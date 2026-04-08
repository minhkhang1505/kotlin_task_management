package com.nguyenminhkhang.taskmanagement.domain.usecase

import com.nguyenminhkhang.shared.model.Task
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository

class UpdateTaskUseCase (
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(
        task: Task
    ): Boolean {
        return taskRepository.updateTask(task)
    }
}