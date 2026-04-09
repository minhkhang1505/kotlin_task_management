package com.nguyenminhkhang.shared.usecase.collectionusecase

import com.nguyenminhkhang.shared.repository.TaskRepository

class DeleteTaskCollectionUseCase (
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(collectionId: Long): Boolean {
        return taskRepository.deleteTaskCollectionById(collectionId)
    }
}