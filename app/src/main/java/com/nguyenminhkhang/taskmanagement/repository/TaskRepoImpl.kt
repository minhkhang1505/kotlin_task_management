package com.nguyenminhkhang.taskmanagement.repository

import com.nguyenminhkhang.taskmanagement.database.dao.TaskDAO
import com.nguyenminhkhang.taskmanagement.database.entity.SortedType
import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Calendar

class TaskRepoImpl(
    private val taskDAO: TaskDAO
) : TaskRepo {
    override fun getTaskCollection(): Flow<List<TaskCollection>>  {
        return taskDAO.getAllTaskCollection()
    }

    override fun getAllTaskByCollectionId(collectionId: Long): Flow<List<TaskEntity>> =
        taskDAO.getAllTaskByCollectionId(collectionId)

    override suspend fun addTask(content: String, collectionId: Long, taskDetail: String, isFavorite: Boolean, startDate: Long?, startTime: Long?, reminderTimeMillis: Long?): TaskEntity? =
        withContext(Dispatchers.IO) {
        val now = Calendar.getInstance().timeInMillis
        val task = TaskEntity(
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
            task.copy(id = id)
        } else null
    }

    override suspend fun addNewCollection(content: String): TaskCollection? =
        withContext(Dispatchers.IO) {
        val now = Calendar.getInstance().timeInMillis
        val taskCollection = TaskCollection(
            content = content,
            updatedAt = now,
            sortedType = SortedType.SORTED_BY_DATE.value
        )
        val id = taskDAO.insertTaskCollection(taskCollection)
        if(id > 0) {
            taskCollection.copy(id = id)
        } else null
    }

    override suspend fun updateTask(task: TaskEntity): Boolean = withContext(Dispatchers.IO) {
        taskDAO.updateTask(task) > 0
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

    override suspend fun updateTaskContentById(taskId: Long, newContent: String): Boolean  {
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskContentById(taskId, newContent) > 0
        }
    }

    override suspend fun updateTaskStartDateById(taskId: Long, startDate: Long): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskStartDateById(taskId, startDate) > 0
        }
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

    override suspend fun updateTaskRepeatById(
        taskId: Long,
        repeatEvery: Long,
        repeatDaysOfWeek: String?,
        repeatInterval: String?,
        repeatStartDay: Long?,
        repeatEndType: String?,
        repeatEndDate: Long?,
        repeatEndCount: Int,
        startTime: Long?
    ): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskRepeatById(
                taskId = taskId,
                repeatEvery = repeatEvery,
                repeatDaysOfWeek = repeatDaysOfWeek,
                repeatInterval = repeatInterval,
                repeatStartDay = repeatStartDay,
                repeatEndType = repeatEndType,
                repeatEndDate = repeatEndDate,
                repeatEndCount = repeatEndCount,
                startTime = startTime,
            ) > 0
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

    override suspend fun updateTaskStartTime(taskId: Long, time: Long): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskStartTime(taskId, time) > 0
        }
    }

    override suspend fun updateTaskDetailById(taskId: Long, detail: String): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskDetailById(taskId, detail) > 0
        }
    }

    override suspend fun updateTaskFavoriteById(taskId: Long, isFavorite: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskFavoriteById(taskId, isFavorite) > 0
        }
    }

    override suspend fun updateTaskRepeatEveryById(taskId: Long, repeatEvery: Long): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskRepeatEveryById(taskId, repeatEvery) > 0
        }
    }

    override suspend fun updateTaskRepeatIntervalById(taskId: Long, repeatInterval: String?): Boolean {
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskRepeatIntervalById(taskId, repeatInterval) > 0
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
        return withContext(Dispatchers.IO) {
            taskDAO.updateTaskCollectionById(taskId, collectionId) > 0
        }
    }
}