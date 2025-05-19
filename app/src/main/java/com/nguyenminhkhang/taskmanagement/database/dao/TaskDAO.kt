package com.nguyenminhkhang.taskmanagement.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity

@Dao
interface TaskDAO {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCollection(taskCollection: TaskCollection)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM task_collection")
    suspend fun getAllTaskCollection(): List<TaskCollection>

    @Query("SELECT * FROM task WHERE collection_id = :collectionId")
    suspend fun getAllTaskByCollectionId(collectionId: Long): List<TaskEntity>

    @Query("UPDATE task SET is_favorite = :isFavorite WHERE id = :taskId")
    suspend fun updateTaskFavorite(taskId: Int, isFavorite: Boolean)

    @Query("UPDATE task SET is_completed = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompleted(taskId: Int, isCompleted: Boolean)

    @Query("UPDATE task_collection SET content = :content WHERE id = :collectionId")
    suspend fun updateTaskCollection(collectionId: Int, content: String)

    @Query("DELETE FROM task WHERE id = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("DELETE FROM task_collection WHERE id = :collectionId")
    suspend fun deleteTaskCollection(collectionId: Int)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Update
    suspend fun updateTaskCollection(taskCollection: TaskCollection)
}