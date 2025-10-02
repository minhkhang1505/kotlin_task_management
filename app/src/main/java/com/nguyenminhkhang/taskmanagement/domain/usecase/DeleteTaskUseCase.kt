package com.nguyenminhkhang.taskmanagement.domain.usecase

import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import jakarta.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long): Boolean {
        return taskRepository.deleteTaskById(taskId)
    }
}