package com.nguyenminhkhang.taskmanagement.repository

import com.nguyenminhkhang.taskmanagement.database.dao.TaskDAO
import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepoImpl(
    private val taskDAO: TaskDAO
) : TaskRepo {
    override suspend fun getTaskCollection(): List<TaskCollection> = withContext(Dispatchers.IO) {
        taskDAO.getAllTaskCollection()
    }

    override suspend fun getTaskCollectionById(collectionId: Long): List<TaskEntity> = withContext(Dispatchers.IO) {
        taskDAO.getAllTaskByCollectionId(collectionId)
    }

}