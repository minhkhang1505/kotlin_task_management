package com.nguyenminhkhang.taskmanagement.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.nguyenminhkhang.shared.model.SortedType
import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.data.local.database.dao.TaskDAO
import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.data.mapper.toDomain
import com.nguyenminhkhang.taskmanagement.data.mapper.toEntity
import com.nguyenminhkhang.shared.model.Task
import com.nguyenminhkhang.shared.model.Collection
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Calendar
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl (
    private val taskDAO: TaskDAO,
) : TaskRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore

    /**
     * Flag to prevent the Firestore snapshot listener from overwriting
     * local Room data during an active save operation.
     */
    @Volatile
    private var isSyncing = false

    override fun getTaskCollection(): Flow<List<Collection>>  {
        Log.d("TaskRepoImpl", "getTaskCollection: ${auth.currentUser?.uid ?: "local_user"}")
        return taskDAO.getAllTaskCollection(auth.currentUser?.uid ?: "local_user")
            .map { collections -> collections.map(TaskCollection::toDomain) }
    }

    override fun getAllTaskByCollectionId(collectionId: Long): Flow<List<Task>> =
        taskDAO.getAllTaskByCollectionId(collectionId, auth.currentUser?.uid ?: "local_user")
            .map { tasks -> tasks.map(TaskEntity::toDomain) }

    override fun syncTasksForCurrentUser() {
        val userId = auth.currentUser?.uid ?: return
        val userEmail = auth.currentUser?.email ?: return

        val userDoc = firestore.collection("users").document(userEmail)

        userDoc.collection("tasks").addSnapshotListener { tasksSnapshot, error ->
            if (error != null) {
                Timber.tag(TAG).w(error, "syncTasksForCurrentUser() - Listen for tasks failed")
                return@addSnapshotListener
            }
            if (isSyncing) {
                Timber.tag(TAG).d("syncTasksForCurrentUser() - SKIPPED: local save in progress")
                return@addSnapshotListener
            }
            if (tasksSnapshot != null) {
                val onlineTasks = tasksSnapshot.toObjects(TaskEntity::class.java)
                Timber.tag(TAG).d("syncTasksForCurrentUser() - Firestore snapshot received: ${onlineTasks.size} tasks, isFromCache=${tasksSnapshot.metadata.isFromCache}")
                CoroutineScope(Dispatchers.IO).launch {
                    taskDAO.syncTasksForUser(userId, onlineTasks)
                    onlineTasks.forEach { task ->
                        Timber.tag(TAG).d("syncTasksForCurrentUser() - Synced task: id=${task.id}, startDate=${task.startDate}, startTime=${task.startTime}, interval=${task.repeatInterval}, updatedAt=${task.updatedAt}")
                    }
                }
            }
        }

        userDoc.collection("task_collections").addSnapshotListener { collectionsSnapshot, error ->
            if (error != null) {
                Log.w("FirestoreSync", "Listen for collections failed.", error)
                return@addSnapshotListener
            }
            if (collectionsSnapshot != null) {
                val onlineCollections = collectionsSnapshot.toObjects(TaskCollection::class.java)
                CoroutineScope(Dispatchers.IO).launch {
                    taskDAO.syncCollectionsForUser(userId, onlineCollections)
                }
            }
        }
    }

    override suspend fun addTask(content: String, collectionId: Long, taskDetail: String, isFavorite: Boolean, startDate: Long?, startTime: Long?, reminderTimeMillis: Long?): Task? =
        withContext(Dispatchers.IO) {
        val now = Calendar.getInstance().timeInMillis
        val task = TaskEntity(
            userId = auth.currentUser?.uid ?: return@withContext null,
            content = content,
            taskDetail = taskDetail,
            favorite = isFavorite,
            completed = false,
            collectionId = collectionId,
            updatedAt = now,
            startDate = startDate,
            startTime = startTime,
            reminderTimeMillis = reminderTimeMillis,
        )
        val id = taskDAO.insertTask(task)

        if (id > 0) {
            val taskWithId = task.copy(id = id)

            firestore.collection("users")
                .document("${auth.currentUser!!.email}")
                .collection("tasks")
                .document(taskWithId.id.toString())
                .set(taskWithId)

            taskWithId.toDomain()
        } else null
    }

    override suspend fun addNewCollection(content: String): Collection? =
        withContext(Dispatchers.IO) {
        val now = Calendar.getInstance().timeInMillis
        val collectionEntity = TaskCollection(
            userId = auth.currentUser?.uid ?: "local_user",
            content = content,
            updatedAt = now,
            sortedType = SortedType.SORTED_BY_DATE.value
        )
        val id = taskDAO.insertTaskCollection(collectionEntity)
        if(id > 0) {
            val taskCollectionWithID = collectionEntity.copy(id = id)

            firestore.collection("users")
                .document("${auth.currentUser!!.email}")
                .collection("task_collections")
                .document(taskCollectionWithID.id.toString()) // Đặt ID cho document
                .set(taskCollectionWithID)

            taskCollectionWithID.toDomain()
        } else null
    }

    override suspend fun updateTaskCollection(collectionEntity: Collection): Boolean =
        withContext(Dispatchers.IO) {
        taskDAO.updateTaskCollection(collectionEntity.toEntity()) > 0
    }

    override suspend fun updateTaskCompleted(taskId: Long, isCompleted: Boolean): Boolean {
        val userEmail = auth.currentUser?.email ?: run {
            Log.d("TaskRepoImpl", "No user ID available")
            return false
        }
        val now = System.currentTimeMillis()
        val taskDocId = taskId.toString()
        val taskPath = "users/$userEmail/tasks/$taskDocId"

        val roomSuccess = withContext(Dispatchers.IO) {
            taskDAO.updateTaskCompleted(taskId, isCompleted, now) > 0
        }
        if (!roomSuccess) {
            Log.d("TaskRepoImpl", "Room update failed for taskId: $taskId")
            return false
        }
        Log.d("TaskRepoImpl", "Room updated successfully: isCompleted=$isCompleted, updatedAt=$now")

        return try {
            val docSnapshot = firestore.collection("users").document(userEmail)
                .collection("tasks").document(taskDocId).get().await()

            if (!docSnapshot.exists()) {
                Log.d("TaskRepoImpl", "Firestore document does not exist: $taskPath")
                return false
            }

            Log.d("TaskRepoImpl", "Firestore document exists, current completed: ${docSnapshot.getBoolean("completed") ?: "null"}")

            firestore.collection("users").document(userEmail)
                .collection("tasks")
                .document(taskDocId)
                .update(mapOf(
                    "completed" to isCompleted,
                    "updatedAt" to now
                ))
                .await()

            Log.d("TaskRepoImpl", "Firestore update successful for $taskPath")
            true
        } catch (e: Exception) {
            Log.e("TaskRepoImpl", "Firestore update failed for $taskPath: ${e.message}", e)
            false
        }
    }

    override suspend fun updateTaskFavorite(taskId: Long, isFavorite: Boolean): Boolean {
        val now = System.currentTimeMillis()
        val userEmail = auth.currentUser?.email ?: return false
        val taskDocId = taskId.toString()

        val roomSuccess = withContext(Dispatchers.IO) {
            taskDAO.updateTaskFavorite(taskId.toInt(), isFavorite, now) > 0
        }

        if (!roomSuccess) {
            Log.d("TaskRepoImpl", "Room update failed for taskId: $taskId")
            return false
        }

        try {
            val docSnapshot = firestore.collection("users").document(userEmail)
                .collection("tasks").document(taskDocId).get().await()

            if (!docSnapshot.exists()) {
                Log.d("TaskRepoImpl", "Firestore document does not exist: users/$userEmail/tasks/$taskDocId")
                return false
            }

            firestore.collection("users").document(userEmail)
                .collection("tasks")
                .document(taskDocId)
                .update(mapOf(
                    "favorite" to isFavorite,
                    "updatedAt" to now
                ))
                .await()

            Log.d("TaskRepoImpl", "Firestore update successful for users/$userEmail/tasks/$taskDocId")
            return true
        } catch (e: Exception) {
            Log.e("TaskRepoImpl", "Firestore update failed for users/$userEmail/tasks/$taskDocId: ${e.message}", e)
            return false
        }
    }

    override suspend fun deleteTaskCollectionById(collectionId: Long): Boolean {
        val userEmail = auth.currentUser?.email ?: return false

        val roomDeleteSuccess = withContext(Dispatchers.IO) {
            taskDAO.deleteTaskCollectionById(collectionId) > 0
        }
        return if (roomDeleteSuccess) {
            try {
                firestore.collection("users").document(userEmail)
                    .collection("task_collections")
                    .document(collectionId.toString())
                    .delete()
                    .await()
                Timber.d( "Collection deleted in Firestore")
                true
            } catch (e: Exception) {
                Log.e("TaskRepoImpl", "Error deleting collection in Firestore", e)
                false
            }
        } else {
            false
        }
    }

    override suspend fun updateCollectionSortedType(collectionId: Long, sortedType: SortedType): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateCollectionSortedType(collectionId, sortedType.value) > 0
        }
        Timber.d("updateCollectionSortedType() - After call to DAO with sortType: ${sortedType}")
    }

    override fun getTaskById(taskId: Long): Flow<Task>{
        Timber.tag(TAG).d("getTaskById() - Fetching task from Room with taskId=$taskId")
        return taskDAO.getTaskById(taskId).map(TaskEntity::toDomain)
    }

    override suspend fun updateTaskDueDateById(taskId: Long, dueDate: Long): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskDueDateById(taskId, dueDate) > 0
        }
    }

    override suspend fun updateTaskReminderTimeById(taskId: Long, reminderTime: Int): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskReminderTimeById(taskId, reminderTime) > 0
        }
    }

    override suspend fun updateTaskPriorityById(taskId: Long, priority: Int): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskPriorityById(taskId, priority) > 0
        }
    }

    override suspend fun updateTask(
        task: Task
    ): Boolean {
        val userEmail = auth.currentUser?.email ?: run {
            Timber.tag(TAG).w("updateTask() - No user email available, aborting")
            return false
        }
        val taskEntity = task.toEntity()

        // Block the sync listener while saving to prevent it from
        // overwriting Room with stale Firestore snapshot data.
        isSyncing = true
        Timber.tag(TAG).d("updateTask() - isSyncing=true, saving to Room: id=${taskEntity.id}, interval=${taskEntity.repeatInterval}, every=${taskEntity.repeatEvery}, endType=${taskEntity.repeatEndType}, startDate=${taskEntity.startDate}, startTime=${taskEntity.startTime}, updatedAt=${taskEntity.updatedAt}")

        val rowsAffected = taskDAO.updateTask(task = taskEntity)
        val roomSuccess = rowsAffected > 0
        Timber.tag(TAG).d("updateTask() - Room update result: rowsAffected=$rowsAffected, success=$roomSuccess")
        if (!roomSuccess) {
            isSyncing = false
            Timber.tag(TAG).e("updateTask() - Room update FAILED for taskId=${taskEntity.id}, 0 rows affected")
            return false
        }
        try {
            Timber.tag(TAG).d("updateTask() - Syncing to Firestore: users/$userEmail/tasks/${taskEntity.id}")
            firestore.collection("users").document(userEmail)
                .collection("tasks")
                .document(taskEntity.id.toString())
                .set(taskEntity)
                .await()
            Timber.tag(TAG).d("updateTask() - Firestore sync successful for taskId=${taskEntity.id}")
        } catch(e: Exception) {
            Timber.tag(TAG).e(e, "updateTask() - Firestore sync failed for taskId=${taskEntity.id}")
        } finally {
            isSyncing = false
            Timber.tag(TAG).d("updateTask() - isSyncing=false")
        }
        return true
    }

    override suspend fun updateTaskStartDate(taskId: Long, startDate: Long): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskStartDate(taskId, startDate) > 0
        }
    }

    override suspend fun clearDateSelected(taskId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.clearDateSelected(taskId) > 0
        }
    }

    override suspend fun clearTimeSelected(taskId: Long) : Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.clearTimeSelected(taskId) > 0
        }
    }

    override suspend fun updateTaskFavoriteById(taskId: Long, isFavorite: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskFavoriteById(taskId, isFavorite) > 0
        }
    }

    override suspend fun deleteTaskById(taskId: Long): Boolean {

        val userEmail = auth.currentUser?.email ?: return false

        val roomDeleteSuccess = withContext(Dispatchers.IO) {
            taskDAO.deleteTaskById(taskId) > 0
        }

        return if(roomDeleteSuccess) {
            try {
                firestore.collection("users").document(userEmail)
                    .collection("tasks")
                    .document(taskId.toString())
                    .delete()
                    .await()
                Log.d("TaskRepoImpl", "Task deleted in Firestore")
                true
            } catch (e: Exception) {
                Log.e("TaskRepoImpl", "Error deleting task in Firestore", e)
                false
            }
        } else {
            false
        }
    }

    override suspend fun getCollectionById(): List<Collection> {
        return withContext(Dispatchers.IO) {
            taskDAO.getCollection().map(TaskCollection::toDomain)
        }
    }

    override suspend fun moveTaskToCollectionById(taskId: Long, collectionId: Long): Boolean {

        val userEmail = auth.currentUser?.email ?: return false
        val roomUpdateSuccess =  withContext(Dispatchers.IO) {
            taskDAO.updateTaskCollectionById(taskId, collectionId) > 0
        }
        return if(roomUpdateSuccess) {
            try {
                val updatedTask = taskDAO.getTaskById(taskId).firstOrNull() ?: return false
                firestore.collection("users").document(userEmail)
                    .collection("tasks")
                    .document(taskId.toString())
                    .set(updatedTask)
                    .await()
                Log.d("TaskRepoImpl", "Collection updated in Firestore")
                true
            } catch(e: Exception) {
                Log.e("TaskRepoImpl", "Error updating collection in Firestore", e)
                false
            }
        } else {
            false
        }
    }

    override suspend fun updateCollectionNameById(collectionId: Long, newCollectionName: String) : Boolean {
        val userEmail = auth.currentUser?.email ?: return false
        val roomUpdateSuccess =  withContext(Dispatchers.IO) {
            taskDAO.updateCollectionName(collectionId, newCollectionName) > 0
        }
        return if(roomUpdateSuccess) {
            try {
                firestore.collection("users").document(userEmail)
                    .collection("task_collections")
                    .document(collectionId.toString())
                    .update(
                        mapOf(
                            "content" to newCollectionName,
                            "updatedAt" to System.currentTimeMillis()
                        )
                    )
                    .await()
                Log.d("TaskRepoImpl", "Collection updated in Firestore")
                true
            } catch(e: Exception) {
                Log.e("TaskRepoImpl", "Error updating collection in Firestore", e)
                false
            }
        } else {
            false
        }
    }

    override fun SearchTasks(query: String): Flow<List<Task>> {
        return taskDAO.SearchTasks(query, auth.currentUser?.uid ?: "local_user")
            .map { tasks -> tasks.map(TaskEntity::toDomain) }
    }

    override suspend fun claimLocalTasks(): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.claimLocalTasks(auth.currentUser!!.uid) > 0
        }
    }

    override suspend fun claimLocalTaskCollection(): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.claimLocalTaskCollection(auth.currentUser!!.uid) > 0
        }
    }

    override fun getTodayTasks(startDate: Long, endDate: Long): Flow<List<Task>> {
        return taskDAO.getTodayTasks(startDate, endDate, auth.currentUser?.uid ?: "local_user")
            .map { tasks -> tasks.map(TaskEntity::toDomain) }
    }

    override suspend fun clearLocalData(): Boolean {
        return CoroutineScope(Dispatchers.IO).launch {
            try {
                taskDAO.clearAllData()
                Log.d("TaskRepoImpl", "Local data cleared successfully")
                true
            } catch (e: Exception) {
                Log.e("TaskRepoImpl", "Error clearing local data", e)
                false
            }
        }.join().let { true }
    }

    companion object {
        private const val TAG = "TaskRepositoryImpl"
    }
}