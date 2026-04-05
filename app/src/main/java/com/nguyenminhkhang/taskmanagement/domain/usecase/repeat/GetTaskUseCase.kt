package com.nguyenminhkhang.taskmanagement.domain.usecase.repeat

import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(taskId: Long): Flow<TaskEntity> {
        Timber.tag(TAG).d("invoke() - Getting task with taskId=$taskId")
        return taskRepository.getTaskById(taskId)
    }

    companion object {
        private const val TAG = "GetTaskUseCase"
    }
}
