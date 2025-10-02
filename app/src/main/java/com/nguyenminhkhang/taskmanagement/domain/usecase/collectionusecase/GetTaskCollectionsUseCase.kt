package com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase

import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskCollectionsUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {

    operator fun invoke() : Flow<List<TaskCollection>> {
        return taskRepository.getTaskCollection()
    }
}