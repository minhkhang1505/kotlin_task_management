package com.nguyenminhkhang.shared.usecase.repeat

import com.nguyenminhkhang.shared.model.Task
import com.nguyenminhkhang.shared.notification.TaskScheduler
import com.nguyenminhkhang.shared.repository.TaskRepository

class UpdateRepeatTaskUseCase (
    private val taskRepository: TaskRepository,
    private val taskScheduler: TaskScheduler
) {
    suspend operator fun invoke(task: Task): Boolean {

        val success = taskRepository.updateTask(task)

        if (success) {
            taskScheduler.schedule(task)
        }
        return success
    }
}
