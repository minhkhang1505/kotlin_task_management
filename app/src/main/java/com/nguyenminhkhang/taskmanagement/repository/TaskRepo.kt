package com.nguyenminhkhang.taskmanagement.repository

import com.nguyenminhkhang.taskmanagement.database.entity.SortedType
import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import kotlinx.coroutines.flow.Flow

interface TaskRepo {
    fun getTaskCollection(): Flow<List<TaskCollection>>
    fun getAllTaskByCollectionId(collectionId: Long): Flow<List<TaskEntity>>
    suspend fun addTask(content:String, collectionId: Long): TaskEntity?
    suspend fun addNewCollection(content: String): TaskCollection?
    suspend fun updateTask(task: TaskEntity): Boolean
    suspend fun updateTaskCollection(taskCollection: TaskCollection): Boolean
    suspend fun updateTaskFavorite(taskId: Long, isFavorite: Boolean): Boolean
    suspend fun updateTaskCompleted(taskId: Long, isCompleted: Boolean): Boolean
    suspend fun deleteTaskCollectionById(collectionId: Long): Boolean
    suspend fun updateCollectionSortedType(collectionId: Long, sortedType: SortedType) : Boolean
    suspend fun getTaskById(taskId: Long): TaskEntity
    suspend fun updateTaskContentById(taskId: Long, newContent: String): Boolean
}