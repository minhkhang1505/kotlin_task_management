package com.nguyenminhkhang.taskmanagement.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCollection(taskCollection: TaskCollection) : Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity) : Long

    @Query("SELECT * FROM task_collection")
    fun getAllTaskCollection(): Flow<List<TaskCollection>>

    @Query("SELECT * FROM task WHERE collection_id = :collectionId")
    fun getAllTaskByCollectionId(collectionId: Long): Flow<List<TaskEntity>>

    @Query("UPDATE task SET is_favorite = :isFavorite WHERE id = :taskId")
    suspend fun updateTaskFavorite(taskId: Int, isFavorite: Boolean) : Int

    @Query("UPDATE task SET is_completed = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompleted(taskId: Int, isCompleted: Boolean) : Int

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

    @Query("""
    UPDATE task 
    SET 
        repeat_every = :repeatEvery,
        repeat_days_of_week = :repeatDaysOfWeek,
        repeat_interval = :repeatInterval,
        start_date = :repeatStartDay,
        repeat_end_type = :repeatEndType,
        repeat_end_date = :repeatEndDate,
        repeat_end_count = :repeatEndCount,
        start_time = :startTime
    WHERE id = :taskId
""")
    suspend fun updateTaskRepeatById(
        taskId: Long,
        repeatEvery: Long,
        repeatDaysOfWeek: String?,
        repeatInterval: String?,
        repeatStartDay: Long?,
        repeatEndType: String?,
        repeatEndDate: Long?,
        repeatEndCount: Int,
        startTime: Long?
    ): Int

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

    @Query("UPDATE task SET is_favorite = :isFavorite WHERE id = :taskId")
    suspend fun updateTaskFavoriteById(taskId: Long, isFavorite: Boolean): Int

    @Query("UPDATE task SET repeat_every = :repeatEvery WHERE id = :taskId")
    suspend fun updateTaskRepeatEveryById(taskId: Long, repeatEvery: Long): Int

    @Query("UPDATE task SET repeat_interval = :repeatInterval WHERE id = :taskId")
    suspend fun updateTaskRepeatIntervalById(taskId: Long, repeatInterval: String?): Int

    @Query("UPDATE task SET collection_id = :collectionId WHERE id = :taskId")
    suspend fun updateTaskCollectionById(taskId: Long, collectionId: Long): Int

    @Query("UPDATE task_collection Set content = :newCollectionName WHERE id = :collectionId")
    suspend fun updateCollectionName(collectionId: Long, newCollectionName: String) : Int

    @Query("SELECT * FROM task WHERE title LIKE '%' || :query || '%'")
    fun SearchTasks(query: String): Flow<List<TaskEntity>>

    @Query("UPDATE task SET user_id = :newUserId WHERE user_id = 'local_user'")
    suspend fun claimLocalTasks(newUserId: String) : Int
}