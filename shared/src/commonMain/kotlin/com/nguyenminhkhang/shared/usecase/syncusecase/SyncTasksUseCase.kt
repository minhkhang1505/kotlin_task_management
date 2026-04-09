package com.nguyenminhkhang.shared.usecase.syncusecase

import com.nguyenminhkhang.shared.repository.TaskRepository

class SyncTasksUseCase (
    private val taskRepository: TaskRepository
) {

    operator fun invoke() {
        taskRepository.syncTasksForCurrentUser()
    }
}