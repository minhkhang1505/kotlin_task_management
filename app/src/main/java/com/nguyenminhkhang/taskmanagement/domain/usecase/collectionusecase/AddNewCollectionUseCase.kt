package com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase

import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import javax.inject.Inject

class AddNewCollectionUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(collectionName: String): TaskCollection? {
        if (collectionName.isBlank()) {
            return null
        }
        return taskRepository.addNewCollection(collectionName)
    }
}