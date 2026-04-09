package com.nguyenminhkhang.shared.usecase

import com.nguyenminhkhang.shared.repository.TaskRepository

class ToggleTaskFavoriteUseCase (
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long, isFavorite: Boolean): Boolean {
        return taskRepository.updateTaskFavorite(taskId, isFavorite)
    }
}