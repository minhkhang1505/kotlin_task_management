package com.nguyenminhkhang.shared.usecase

import com.nguyenminhkhang.shared.repository.TaskRepository

class DeleteTaskUseCase (
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long): Boolean {
        return taskRepository.deleteTaskById(taskId)
    }
}