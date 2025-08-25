package com.nguyenminhkhang.taskmanagement.repository

import com.nguyenminhkhang.taskmanagement.database.entity.SortedType
import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepo {
    fun getTaskCollection(): Flow<List<TaskCollection>>
    fun getAllTaskByCollectionId(collectionId: Long): Flow<List<TaskEntity>>
    fun syncTasksForCurrentUser()
    suspend fun addTask(content:String, collectionId: Long, taskDetail: String, isFavorite: Boolean, startDate: Long?, startTime: Long?, reminderTimeMillis: Long? ): TaskEntity?
    suspend fun addNewCollection(content: String): TaskCollection?
    suspend fun updateTaskCollection(taskCollection: TaskCollection): Boolean
    suspend fun updateTaskFavorite(taskId: Long, isFavorite: Boolean): Boolean
    suspend fun updateTaskCompleted(taskId: Long, isCompleted: Boolean): Boolean
    suspend fun deleteTaskCollectionById(collectionId: Long): Boolean
    suspend fun updateCollectionSortedType(collectionId: Long, sortedType: SortedType) : Boolean
    fun getTaskById(taskId: Long): Flow<TaskEntity>
//    suspend fun updateTaskContentById(taskId: Long, newContent: String): Boolean
//    suspend fun updateTaskStartDateById(taskId: Long, startDate: Long): Boolean
    suspend fun updateTaskDueDateById(taskId: Long, dueDate: Long): Boolean
    suspend fun updateTaskReminderTimeById(taskId: Long, reminderTime: Int): Boolean
    suspend fun updateTaskPriorityById(taskId: Long, priority: Int): Boolean
    suspend fun updateTask(task: TaskEntity): Boolean
    suspend fun updateTaskStartDate(taskId : Long, startDate : Long): Boolean
    suspend fun clearDateSelected(taskId : Long): Boolean
    suspend fun clearTimeSelected(taskId : Long) : Boolean
//    suspend fun updateTaskStartTime(taskId: Long, time: Long): Boolean
//    suspend fun updateTaskDetailById(taskId: Long, detail: String): Boolean
    suspend fun updateTaskFavoriteById(taskId: Long, isFavorite: Boolean): Boolean
    suspend fun updateTaskRepeatEveryById(taskId: Long, repeatEvery: Long): Boolean
    suspend fun updateTaskRepeatIntervalById(taskId: Long, repeatInterval: String?): Boolean
    suspend fun deleteTaskById(taskId: Long): Boolean
    suspend fun getCollectionById(): List<TaskCollection>
    suspend fun updateTaskCollectionById(taskId: Long, collectionId: Long): Boolean
    suspend fun updateCollectionNameById(collectionId: Long, newCollectionName: String): Boolean
    fun SearchTasks(query: String): Flow<List<TaskEntity>>
    suspend fun claimLocalTasks() : Boolean
    suspend fun claimLocalTaskCollection() : Boolean
    fun getTodayTasks(startDate: Long, endDate: Long): Flow<List<TaskEntity>>
}