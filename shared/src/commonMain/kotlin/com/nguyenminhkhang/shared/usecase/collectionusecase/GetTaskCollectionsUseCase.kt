package com.nguyenminhkhang.shared.usecase.collectionusecase

import com.nguyenminhkhang.shared.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import com.nguyenminhkhang.shared.model.Collection

class GetTaskCollectionsUseCase (
    private val taskRepository: TaskRepository
) {

    operator fun invoke() : Flow<List<Collection>> {
        return taskRepository.getTaskCollection()
    }
}