package com.nguyenminhkhang.shared.usecase

import com.nguyenminhkhang.shared.model.Task
import com.nguyenminhkhang.shared.repository.TaskRepository

class AddTaskUseCase (
    private val taskRepository: TaskRepository
) {
    /**
     * Adds a new task to a collection.
     * @param content The task content. Blank values are rejected.
     * @param collectionId The target collection id.
     * @return The created task or null if input is invalid.
     */
    suspend operator fun invoke(
        content: String,
        collectionId: Long,
        taskDetail: String,
        isFavorite: Boolean,
        startDate: Long?,
        startTime: Long?,
        reminderTimeMillis: Long?
    ) : Task? {
        if (content.isBlank()) {
            return null
        }
        return taskRepository.addTask(content, collectionId, taskDetail, isFavorite, startDate, startTime, reminderTimeMillis)
    }
}