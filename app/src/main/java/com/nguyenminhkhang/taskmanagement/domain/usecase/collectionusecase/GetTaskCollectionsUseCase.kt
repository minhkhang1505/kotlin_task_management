package com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase

import com.nguyenminhkhang.taskmanagement.domain.model.Collection
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskCollectionsUseCase (
    private val taskRepository: TaskRepository
) {

    operator fun invoke() : Flow<List<Collection>> {
        return taskRepository.getTaskCollection()
    }
}