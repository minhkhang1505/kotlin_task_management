package com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase

import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskCollectionUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(collectionId: Long): Boolean {
        return taskRepository.deleteTaskCollectionById(collectionId)
    }
}