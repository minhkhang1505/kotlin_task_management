package com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase

import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import com.nguyeminhkhang.shared.model.Collection

class GetTaskCollectionsUseCase (
    private val taskRepository: TaskRepository
) {

    operator fun invoke() : Flow<List<Collection>> {
        return taskRepository.getTaskCollection()
    }
}