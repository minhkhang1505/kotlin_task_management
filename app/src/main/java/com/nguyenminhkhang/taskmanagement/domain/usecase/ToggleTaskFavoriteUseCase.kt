package com.nguyenminhkhang.taskmanagement.domain.usecase

import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import javax.inject.Inject

class ToggleTaskFavoriteUseCase (
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long, isFavorite: Boolean): Boolean {
        return taskRepository.updateTaskFavorite(taskId, isFavorite)
    }
}