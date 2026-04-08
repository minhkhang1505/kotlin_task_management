package com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase

import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
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