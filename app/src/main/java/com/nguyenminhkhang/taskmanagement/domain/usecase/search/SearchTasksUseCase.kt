package com.nguyenminhkhang.taskmanagement.domain.usecase.search

import com.nguyenminhkhang.taskmanagement.domain.model.Task
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(query: String): Flow<List<Task>> {
        return taskRepository.SearchTasks(query)
    }
}