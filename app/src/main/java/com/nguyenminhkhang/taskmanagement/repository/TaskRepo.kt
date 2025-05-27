package com.nguyenminhkhang.taskmanagement.repository

import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity

interface TaskRepo {
    suspend fun getTaskCollection(): List<TaskCollection>
    suspend fun getTaskCollectionById(collectionId: Long): List<TaskEntity>
    suspend fun addTask(content:String, collectionId: Long): TaskEntity?
    suspend fun addNewCollection(content: String): TaskCollection?
    suspend fun updateTask(task: TaskEntity): Boolean
    suspend fun updateTaskCollection(taskCollection: TaskCollection): Boolean
    suspend fun updateTaskFavorite(taskId: Long, isFavorite: Boolean): Boolean
    suspend fun updateTaskCompleted(taskId: Long, isCompleted: Boolean): Boolean
}