package com.nguyenminhkhang.shared.usecase

import com.nguyenminhkhang.shared.model.Task
import com.nguyenminhkhang.shared.repository.TaskRepository
import kotlinx.coroutines.flow.firstOrNull

enum class TaskProperty {
    COMPLETED,
    FAVORITE,
    TITLE,
    DESCRIPTION,
    DUE_DATE,
    PRIORITY
}

data class UpdateTaskPropertiesParams(
    val property: TaskProperty,
    val value: Any
)

class UpdateTaskPropertiesUseCase (
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long, params: UpdateTaskPropertiesParams): Boolean {
        return when (params.property) {
            TaskProperty.COMPLETED -> {
                val isCompleted = params.value as? Boolean ?: return false
                taskRepository.updateTaskCompleted(taskId, isCompleted)
            }

            TaskProperty.FAVORITE -> {
                val isFavorite = params.value as? Boolean ?: return false
                taskRepository.updateTaskFavoriteById(taskId, isFavorite)
            }

            TaskProperty.TITLE -> {
                val title = params.value as? String ?: return false
                val currentTask = taskRepository.getTaskById(taskId).firstOrNull() ?: return false
                taskRepository.updateTask(currentTask.copy(content = title))
            }

            TaskProperty.DESCRIPTION -> {
                val description = params.value as? String ?: return false
                val currentTask = taskRepository.getTaskById(taskId).firstOrNull() ?: return false
                taskRepository.updateTask(currentTask.copy(taskDetail = description))
            }

            TaskProperty.DUE_DATE -> {
                val dueDate = params.value as? Long ?: return false
                taskRepository.updateTaskDueDateById(taskId, dueDate)
            }

            TaskProperty.PRIORITY -> {
                val priority = params.value as? Int ?: return false
                taskRepository.updateTaskPriorityById(taskId, priority)
            }
        }
    }
}