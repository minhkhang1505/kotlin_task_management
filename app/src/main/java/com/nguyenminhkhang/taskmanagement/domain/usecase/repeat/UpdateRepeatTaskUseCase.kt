package com.nguyenminhkhang.taskmanagement.domain.usecase.repeat

import com.nguyeminhkhang.shared.model.Task
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import com.nguyenminhkhang.taskmanagement.notification.TaskScheduler
import timber.log.Timber

class UpdateRepeatTaskUseCase (
    private val taskRepository: TaskRepository,
    private val taskScheduler: TaskScheduler
) {
    suspend operator fun invoke(task: Task): Boolean {
        Timber.tag(TAG).d("invoke() - Updating task: id=${task.id}, interval=${task.repeatInterval}, every=${task.repeatEvery}, endType=${task.repeatEndType}, endCount=${task.repeatEndCount}, endDate=${task.repeatEndDate}, startDate=${task.startDate}, startTime=${task.startTime}, daysOfWeek=${task.repeatDaysOfWeek}")

        val success = taskRepository.updateTask(task)
        Timber.tag(TAG).d("invoke() - Repository updateTask returned: success=$success")

        if (success) {
            Timber.tag(TAG).d("invoke() - Scheduling task notification for taskId=${task.id}")
            taskScheduler.schedule(task)
            Timber.tag(TAG).d("invoke() - Task scheduled successfully")
        } else {
            Timber.tag(TAG).w("invoke() - Repository updateTask failed, skipping scheduler")
        }
        return success
    }

    companion object {
        private const val TAG = "UpdateRepeatTaskUseCase"
    }
}
