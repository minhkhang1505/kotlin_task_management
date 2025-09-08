package com.nguyenminhkhang.taskmanagement.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.nguyenminhkhang.taskmanagement.database.dao.TaskDAO
import com.nguyenminhkhang.taskmanagement.database.entity.SortedType
import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar

class TaskRepoImpl (
    private val taskDAO: TaskDAO,
) : TaskRepo {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore

    override fun getTaskCollection(): Flow<List<TaskCollection>>  {
        Log.d("TaskRepoImpl", "getTaskCollection: ${auth.currentUser?.uid ?: "local_user"}")
        return taskDAO.getAllTaskCollection(auth.currentUser?.uid ?: "local_user")
    }

    override fun getAllTaskByCollectionId(collectionId: Long): Flow<List<TaskEntity>> =
        taskDAO.getAllTaskByCollectionId(collectionId, auth.currentUser?.uid ?: "local_user")

    override fun syncTasksForCurrentUser() {
        val userId = auth.currentUser?.uid ?: return
        val userEmail = auth.currentUser?.email ?: return

        val userDoc = firestore.collection("users").document(userEmail)

        userDoc.collection("tasks").addSnapshotListener { tasksSnapshot, error ->
            if (error != null) {
                Log.w("FirestoreSync", "Listen for tasks failed.", error)
                return@addSnapshotListener
            }
            if (tasksSnapshot != null) {
                val onlineTasks = tasksSnapshot.toObjects(TaskEntity::class.java)
                CoroutineScope(Dispatchers.IO).launch {
                    taskDAO.syncTasksForUser(userId, onlineTasks)
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

    override suspend fun addTask(content: String, collectionId: Long, taskDetail: String, isFavorite: Boolean, startDate: Long?, startTime: Long?, reminderTimeMillis: Long?): TaskEntity? =
        withContext(Dispatchers.IO) {
        val now = Calendar.getInstance().timeInMillis
        val task = TaskEntity(
            userId = auth.currentUser?.uid ?: return@withContext null,
            content = content,
            taskDetail = taskDetail,
            isFavorite = isFavorite,
            isCompleted = false,
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

            taskWithId
        } else null
    }

    override suspend fun addNewCollection(content: String): TaskCollection? =
        withContext(Dispatchers.IO) {
        val now = Calendar.getInstance().timeInMillis
        val taskCollection = TaskCollection(
            userId = auth.currentUser?.uid ?: "local_user",
            content = content,
            updatedAt = now,
            sortedType = SortedType.SORTED_BY_DATE.value
        )
        val id = taskDAO.insertTaskCollection(taskCollection)
        if(id > 0) {
            val taskCollectionWithID = taskCollection.copy(id = id)

            firestore.collection("users")
                .document("${auth.currentUser!!.email}")
                .collection("task_collections")
                .document(taskCollectionWithID.id.toString()) // Đặt ID cho document
                .set(taskCollectionWithID)

            taskCollectionWithID
        } else null
    }

    override suspend fun updateTaskCollection(taskCollection: TaskCollection): Boolean =
        withContext(Dispatchers.IO) {
        taskDAO.updateTaskCollection(taskCollection) > 0
    }

    override suspend fun updateTaskCompleted(taskId: Long, isCompleted: Boolean): Boolean =
        withContext(Dispatchers.IO) {
        taskDAO.updateTaskCompleted(taskId.toInt(), isCompleted) > 0
    }

    override suspend fun updateTaskFavorite(taskId: Long, isFavorite: Boolean): Boolean =
        withContext(Dispatchers.IO) {
        taskDAO.updateTaskFavorite(taskId.toInt(), isFavorite) > 0
    }

    override suspend fun deleteTaskCollectionById(collectionId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.deleteTaskCollectionById(collectionId) > 0
        }
    }

    override suspend fun updateCollectionSortedType(collectionId: Long, sortedType: SortedType): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateCollectionSortedType(collectionId, sortedType.value) > 0
        }
    }

    override fun getTaskById(taskId: Long): Flow<TaskEntity>{
        return taskDAO.getTaskById(taskId)
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
        task: TaskEntity
    ): Boolean {
        val userEmail = auth.currentUser?.email ?: return false

        val roomUpdateSuccess =  withContext(Dispatchers.IO) {
            taskDAO.updateTask(task = task) > 0
        }

        return if(roomUpdateSuccess) {
            try {
                firestore.collection("users").document(userEmail)
                    .collection("tasks")
                    .document(task.id.toString())
                    .set(task)
                    .await()
                Log.d("TaskRepoImpl", "Task updated in Firestore")
                true
            } catch(e: Exception) {
                Log.e("TaskRepoImpl", "Error updating task in Firestore", e)
                false
            }
        } else {
            false
        }
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
        return withContext(Dispatchers.IO) {
            taskDAO.deleteTaskById(taskId) > 0
        }
    }

    override suspend fun getCollectionById(): List<TaskCollection> {
        return withContext(Dispatchers.IO) {
            taskDAO.getCollection()
        }
    }

    override suspend fun updateTaskCollectionById(taskId: Long, collectionId: Long): Boolean {

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
        return withContext(Dispatchers.IO) {
            taskDAO.updateCollectionName(collectionId, newCollectionName) > 0
        }
    }

    override fun SearchTasks(query: String): Flow<List<TaskEntity>> {
        return taskDAO.SearchTasks(query, auth.currentUser?.uid ?: "local_user")
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

    override fun getTodayTasks(startDate: Long, endDate: Long): Flow<List<TaskEntity>> {
        return taskDAO.getTodayTasks(startDate, endDate, auth.currentUser?.uid ?: "local_user")
    }
}