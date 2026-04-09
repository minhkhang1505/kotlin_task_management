package com.nguyenminhkhang.shared.usecase

import com.nguyenminhkhang.shared.model.Task
import com.nguyenminhkhang.shared.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTaskByIdUseCase (
    private val taskRepository: TaskRepository
) {
    operator fun invoke(taskId: Long): Flow<Task> = taskRepository.getTaskById(taskId)
}