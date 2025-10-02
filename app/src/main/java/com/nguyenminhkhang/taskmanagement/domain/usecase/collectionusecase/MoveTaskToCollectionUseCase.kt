package com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase

import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import javax.inject.Inject

class MoveTaskToCollectionUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long, newCollectionId: Long): Boolean {
        return taskRepository.moveTaskToCollectionById(taskId, newCollectionId)
    }
}