package com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase

import com.nguyenminhkhang.taskmanagement.domain.model.SortedType
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateCollectionSortTypeUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(collectionId: Long, newSortType: SortedType): Boolean {
        return taskRepository.updateCollectionSortedType(collectionId, newSortType)
    }
}