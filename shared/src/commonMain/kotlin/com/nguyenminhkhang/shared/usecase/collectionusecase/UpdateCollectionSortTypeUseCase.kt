package com.nguyenminhkhang.shared.usecase.collectionusecase

import com.nguyenminhkhang.shared.model.SortedType
import com.nguyenminhkhang.shared.repository.TaskRepository

class UpdateCollectionSortTypeUseCase (
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(collectionId: Long, newSortType: SortedType): Boolean {
        return taskRepository.updateCollectionSortedType(collectionId, newSortType)
    }
}