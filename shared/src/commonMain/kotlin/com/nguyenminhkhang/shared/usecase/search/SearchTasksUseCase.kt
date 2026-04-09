package com.nguyenminhkhang.shared.usecase.search

import com.nguyenminhkhang.shared.model.Task
import com.nguyenminhkhang.shared.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class SearchTasksUseCase (
    private val taskRepository: TaskRepository
) {
    operator fun invoke(query: String): Flow<List<Task>> {
        return taskRepository.searchTasks(query)
    }
}