package com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase

import com.nguyenminhkhang.shared.model.SortedType
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import timber.log.Timber

class UpdateCollectionSortTypeUseCase (
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(collectionId: Long, newSortType: SortedType): Boolean {
        Timber.d("UpdateCollectionSortTypeUseCase - call to repository")
        return taskRepository.updateCollectionSortedType(collectionId, newSortType)
    }
}