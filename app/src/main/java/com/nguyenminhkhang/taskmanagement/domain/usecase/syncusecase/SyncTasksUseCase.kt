package com.nguyenminhkhang.taskmanagement.domain.usecase.syncusecase

import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import javax.inject.Inject

class SyncTasksUseCase (
    private val taskRepository: TaskRepository
) {

    operator fun invoke() {
        return taskRepository.syncTasksForCurrentUser()
    }
}