package com.nguyenminhkhang.taskmanagement.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {

    @Transaction
    suspend fun syncTasksForUser(userId: String, tasks: List<TaskEntity>) {
        clearTasksForUser(userId)
        insertAllTasks(tasks)
    }

    @Transaction
    suspend fun syncCollectionsForUser(userId: String, taskCollection: List<TaskCollection>) {
        ClearAllCollections(userId)
        insertAllCollection(taskCollection)
    }

    @Transaction
    suspend fun clearAllData() {
        clearAllTasks()
        clearAllCollections()
    }

    @Query("DELETE FROM task WHERE user_id = :userId")
    suspend fun clearTasksForUser(userId: String)

    @Query("DELETE FROM task_collection WHERE user_id = :userId")
    suspend fun ClearAllCollections(userId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTasks(task: List<TaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCollection(taskCollection: List<TaskCollection>)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCollection(taskCollection: TaskCollection) : Long

    @Query("DELETE FROM task")
    suspend fun clearAllTasks()

    @Query("DELETE FROM task_collection")
    suspend fun clearAllCollections()

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity) : Long

    @Query("SELECT * FROM task_collection WHERE user_id = :currentUser")
    fun getAllTaskCollection(currentUser: String): Flow<List<TaskCollection>>

    @Query("SELECT * FROM task WHERE collection_id = :collectionId AND user_id = :currentUser")
    fun getAllTaskByCollectionId(collectionId: Long, currentUser: String): Flow<List<TaskEntity>>

    @Query("UPDATE task SET favorite = :isFavorite WHERE id = :taskId")
    suspend fun updateTaskFavorite(taskId: Int, isFavorite: Boolean) : Int

    @Query("UPDATE task SET completed = :isCompleted, updated_at = :updatedAt WHERE id = :taskId")
    suspend fun updateTaskCompleted(taskId: Long, isCompleted: Boolean, updatedAt: Long): Int

    @Query("UPDATE task_collection SET content = :content WHERE id = :collectionId")
    suspend fun updateTaskCollection(collectionId: Int, content: String)

    @Delete
    suspend fun deleteTask(task: TaskEntity) : Int

    @Query("DELETE FROM task WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long) : Int

    @Delete
    suspend fun deleteTaskCollection(taskCollection: TaskCollection) : Int

    @Query("DELETE FROM task_collection WHERE id = :collectionId")
    suspend fun deleteTaskCollectionById(collectionId: Long) : Int

    @Update
    suspend fun updateTask(task: TaskEntity) : Int

    @Update
    suspend fun updateTaskCollection(taskCollection: TaskCollection) : Int

    @Query("UPDATE task_collection SET sorted_type = :sortType WHERE id = :collectionId")
    suspend fun updateCollectionSortedType(collectionId: Long, sortType: Int) : Int

    @Query("SELECT * FROM task WHERE id = :taskId")
    fun getTaskById(taskId: Long): Flow<TaskEntity>

    @Query("SELECT * FROM task_collection")
    fun getCollection() : List<TaskCollection>

    @Query("UPDATE task SET title =  :newContent WHERE id = :taskId")
    suspend fun updateTaskContentById(taskId: Long, newContent: String) : Int

    @Query("UPDATE task SET start_date = :startDate WHERE id = :taskId")
    suspend fun updateTaskStartDateById(taskId: Long, startDate: Long): Int

    @Query("UPDATE task SET due_date = :dueDate WHERE id = :taskId")
    suspend fun updateTaskDueDateById(taskId: Long, dueDate: Long): Int

    @Query("UPDATE task SET reminder_time = :reminderTime WHERE id = :taskId")
    suspend fun updateTaskReminderTimeById(taskId: Long, reminderTime: Int): Int

    @Query("UPDATE task SET priority = :priority WHERE id = :taskId")
    suspend fun updateTaskPriorityById(taskId: Long, priority: Int): Int

    @Query("UPDATE task SET start_date = :startDate WHERE id = :taskId")
    suspend fun updateTaskStartDate(taskId: Long, startDate: Long): Int

    @Query("UPDATE task SET start_date = NULL WHERE id = :taskId")
    suspend fun clearDateSelected(taskId: Long): Int

    @Query("UPDATE task SET start_time = NULL WHERE id = :taskId")
    suspend fun clearTimeSelected(taskId: Long) : Int

    @Query("UPDATE task SET start_time = :time WHERE id = :taskId")
    suspend fun updateTaskStartTime(taskId: Long, time: Long): Int

    @Query("UPDATE task SET task_detail = :detail WHERE id = :taskId")
    suspend fun updateTaskDetailById(taskId: Long, detail: String): Int

    @Query("UPDATE task SET favorite = :isFavorite WHERE id = :taskId")
    suspend fun updateTaskFavoriteById(taskId: Long, isFavorite: Boolean): Int

    @Query("UPDATE task SET repeat_every = :repeatEvery WHERE id = :taskId")
    suspend fun updateTaskRepeatEveryById(taskId: Long, repeatEvery: Long): Int

    @Query("UPDATE task SET repeat_interval = :repeatInterval WHERE id = :taskId")
    suspend fun updateTaskRepeatIntervalById(taskId: Long, repeatInterval: String?): Int

    @Query("UPDATE task SET collection_id = :collectionId WHERE id = :taskId")
    suspend fun updateTaskCollectionById(taskId: Long, collectionId: Long): Int

    @Query("UPDATE task_collection Set content = :newCollectionName WHERE id = :collectionId")
    suspend fun updateCollectionName(collectionId: Long, newCollectionName: String) : Int

    @Query("SELECT * FROM task WHERE title LIKE '%' || :query || '%' AND user_id = :currentUser")
    fun SearchTasks(query: String, currentUser: String): Flow<List<TaskEntity>>

    @Query("UPDATE task SET user_id = :newUserId WHERE user_id = 'local_user'")
    suspend fun claimLocalTasks(newUserId: String) : Int

    @Query("UPDATE task_collection SET user_id = :newUserId WHERE user_id = 'local_user'")
    suspend fun claimLocalTaskCollection(newUserId: String) : Int

    @Query("""
    SELECT * FROM task 
    WHERE repeat_end_date BETWEEN :startOfDay AND :endOfDay AND user_id = :currentUser
""")
    fun getTodayTasks(
        startOfDay: Long,
        endOfDay: Long,
        currentUser: String
    ): Flow<List<TaskEntity>>
}