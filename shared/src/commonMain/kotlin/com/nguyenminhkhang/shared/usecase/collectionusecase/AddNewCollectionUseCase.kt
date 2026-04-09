package com.nguyenminhkhang.shared.usecase.collectionusecase

import com.nguyenminhkhang.shared.repository.TaskRepository
import com.nguyenminhkhang.shared.model.Collection

class AddNewCollectionUseCase (
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(collectionName: String): Collection? {
        if (collectionName.isBlank()) {
            return null
        }
        return taskRepository.addNewCollection(collectionName)
    }
}