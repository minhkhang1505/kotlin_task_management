package com.nguyenminhkhang.taskmanagement.domain.usecase

import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import jakarta.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    /**
     * Returns the list of tasks for a given collection ID.
     * @param collectionId The ID of the collection to fetch tasks for.
     * @return A TaskEntity objects belonging to the specified collection.
     * @throws Exception if there is an error fetching the tasks.
     */
    suspend operator fun invoke(
        content: String,
        collectionId: Long,
        taskDetail: String,
        isFavorite: Boolean,
        startDate: Long?,
        startTime: Long?,
        reminderTimeMillis: Long?
    ) : TaskEntity? {
        if (content.isBlank()) {
            return null
        }
        return taskRepository.addTask(content, collectionId, taskDetail, isFavorite, startDate, startTime, reminderTimeMillis)
    }
}