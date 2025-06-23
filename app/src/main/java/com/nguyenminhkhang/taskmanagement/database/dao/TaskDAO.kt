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
    suspend fun getTaskById(taskId: Long): TaskEntity

    @Query("UPDATE task SET title =  :newContent WHERE id = :taskId")
    suspend fun updateTaskContentById(taskId: Long, newContent: String) : Int
}