package com.nguyenminhkhang.taskmanagement.repository

import com.nguyenminhkhang.taskmanagement.database.dao.TaskDAO
import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class TaskRepoImpl(
    private val taskDAO: TaskDAO
) : TaskRepo {
    override suspend fun getTaskCollection(): List<TaskCollection> = withContext(Dispatchers.IO) {
        taskDAO.getAllTaskCollection()
    }

    override suspend fun getTaskCollectionById(collectionId: Long): List<TaskEntity> = withContext(Dispatchers.IO) {
        taskDAO.getAllTaskByCollectionId(collectionId)
    }

    override suspend fun addTask(content: String, collectionId: Long): TaskEntity? = withContext(Dispatchers.IO) {
        val now = Calendar.getInstance().timeInMillis
        val task = TaskEntity(
            content = content,
            isFavorite = false,
            isCompleted = false,
            collectionId = collectionId,
            updatedAt = now
        )
        val id = taskDAO.insertTask(task)
        if (id > 0) {
            task.copy(id = id)
        } else {
            null
        }
    }

    override suspend fun addNewCollection(content: String): TaskCollection? = withContext(Dispatchers.IO) {
        val now = Calendar.getInstance().timeInMillis
        val taskCollection = TaskCollection(
            content = content,
            updatedAt = now
        )
        val id = taskDAO.insertTaskCollection(taskCollection)
        if(id > 0) {
            taskCollection.copy(id = id)
        } else {
            null
        }
    }
}