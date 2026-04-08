package com.nguyenminhkhang.taskmanagement.domain.usecase.search

import com.nguyeminhkhang.shared.model.Task
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class SearchTasksUseCase (
    private val taskRepository: TaskRepository
) {
    operator fun invoke(query: String): Flow<List<Task>> {
        return taskRepository.SearchTasks(query)
    }
}