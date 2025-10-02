package com.nguyenminhkhang.taskmanagement.domain.usecase

import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(taskId: Long) = taskRepository.getTaskById(taskId)
}