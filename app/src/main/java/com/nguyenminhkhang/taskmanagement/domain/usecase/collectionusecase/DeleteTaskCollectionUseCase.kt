package com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase

import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import timber.log.Timber
import javax.inject.Inject

class DeleteTaskCollectionUseCase (
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(collectionId: Long): Boolean {
        Timber.d("DeleteTaskCollectionUseCase - call taskRepository to delete collection with id: ${collectionId}")
        return taskRepository.deleteTaskCollectionById(collectionId)
    }
}