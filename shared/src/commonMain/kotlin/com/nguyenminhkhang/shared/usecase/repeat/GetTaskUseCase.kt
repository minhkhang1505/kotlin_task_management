package com.nguyenminhkhang.shared.usecase.repeat

import com.nguyenminhkhang.shared.model.Task
import com.nguyenminhkhang.shared.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTaskUseCase (
    private val taskRepository: TaskRepository
) {
    operator fun invoke(taskId: Long): Flow<Task> {
        return taskRepository.getTaskById(taskId)
    }

    companion object {
        private const val TAG = "GetTaskUseCase"
    }
}
