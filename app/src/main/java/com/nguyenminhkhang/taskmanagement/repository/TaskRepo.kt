package com.nguyenminhkhang.taskmanagement.repository

import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity

interface TaskRepo {
    suspend fun getTaskCollection(): List<TaskCollection>
    suspend fun getTaskCollectionById(collectionId: Long): List<TaskEntity>
    suspend fun addTask(content:String, collectionId: Long): TaskEntity?
    suspend fun addNewCollection(content: String): TaskCollection?
}