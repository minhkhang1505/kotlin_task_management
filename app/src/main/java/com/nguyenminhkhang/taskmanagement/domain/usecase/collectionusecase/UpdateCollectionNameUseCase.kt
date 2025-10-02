package com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase

import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateCollectionNameUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {

    suspend operator fun invoke(collectionId: Long, newName: String): Boolean {
        if (newName.isBlank()) {
            return false
        }
        return taskRepository.updateCollectionNameById(collectionId, newName)
    }
}