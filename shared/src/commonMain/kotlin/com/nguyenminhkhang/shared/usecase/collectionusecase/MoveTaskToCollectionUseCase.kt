package com.nguyenminhkhang.shared.usecase.collectionusecase

import com.nguyenminhkhang.shared.repository.TaskRepository

class MoveTaskToCollectionUseCase (
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long, newCollectionId: Long): Boolean {
        return taskRepository.moveTaskToCollectionById(taskId, newCollectionId)
    }
}