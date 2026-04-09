package com.nguyenminhkhang.shared.usecase.collectionusecase

import com.nguyenminhkhang.shared.repository.TaskRepository

class UpdateCollectionNameUseCase (
    private val taskRepository: TaskRepository
) {

    suspend operator fun invoke(collectionId: Long, newName: String): Boolean {
        if (newName.isBlank()) {
            return false
        }
        return taskRepository.updateCollectionNameById(collectionId, newName)
    }
}