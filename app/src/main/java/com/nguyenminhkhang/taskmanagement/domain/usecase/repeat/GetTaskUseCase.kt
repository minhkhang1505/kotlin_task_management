package com.nguyenminhkhang.taskmanagement.domain.usecase.repeat

import com.nguyeminhkhang.shared.model.Task
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class GetTaskUseCase (
    private val taskRepository: TaskRepository
) {
    operator fun invoke(taskId: Long): Flow<Task> {
        Timber.tag(TAG).d("invoke() - Getting task with taskId=$taskId")
        return taskRepository.getTaskById(taskId)
    }

    companion object {
        private const val TAG = "GetTaskUseCase"
    }
}
