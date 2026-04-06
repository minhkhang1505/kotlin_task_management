package com.nguyenminhkhang.taskmanagement.domain.repository

import com.nguyenminhkhang.taskmanagement.domain.model.Collection
import com.nguyenminhkhang.taskmanagement.domain.model.SortedType
import com.nguyenminhkhang.taskmanagement.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTaskCollection(): Flow<List<Collection>>
    fun getAllTaskByCollectionId(collectionId: Long): Flow<List<Task>>
    fun syncTasksForCurrentUser()
    fun getTaskById(taskId: Long): Flow<Task>
    fun getTodayTasks(startDate: Long, endDate: Long): Flow<List<Task>>
    fun SearchTasks(query: String): Flow<List<Task>>

    suspend fun addTask(content:String, collectionId: Long, taskDetail: String, isFavorite: Boolean, startDate: Long?, startTime: Long?, reminderTimeMillis: Long? ): Task?
    suspend fun addNewCollection(content: String): Collection?
    suspend fun updateTask(task: Task): Boolean
    suspend fun updateTaskCollection(taskCollection: Collection): Boolean
    suspend fun updateTaskFavorite(taskId: Long, isFavorite: Boolean): Boolean
    suspend fun updateTaskCompleted(taskId: Long, isCompleted: Boolean): Boolean
    suspend fun deleteTaskCollectionById(collectionId: Long): Boolean
    suspend fun updateCollectionSortedType(collectionId: Long, sortedType: SortedType) : Boolean
    suspend fun updateTaskDueDateById(taskId: Long, dueDate: Long): Boolean
    suspend fun updateTaskReminderTimeById(taskId: Long, reminderTime: Int): Boolean
    suspend fun updateTaskPriorityById(taskId: Long, priority: Int): Boolean
    suspend fun updateTaskStartDate(taskId : Long, startDate : Long): Boolean
    suspend fun clearDateSelected(taskId : Long): Boolean
    suspend fun clearTimeSelected(taskId : Long) : Boolean
    suspend fun updateTaskFavoriteById(taskId: Long, isFavorite: Boolean): Boolean
    suspend fun deleteTaskById(taskId: Long): Boolean
    suspend fun getCollectionById(): List<Collection>
    suspend fun moveTaskToCollectionById(taskId: Long, collectionId: Long): Boolean
    suspend fun updateCollectionNameById(collectionId: Long, newCollectionName: String): Boolean

    suspend fun claimLocalTasks() : Boolean
    suspend fun claimLocalTaskCollection() : Boolean

    //logout
    suspend fun clearLocalData() : Boolean
}