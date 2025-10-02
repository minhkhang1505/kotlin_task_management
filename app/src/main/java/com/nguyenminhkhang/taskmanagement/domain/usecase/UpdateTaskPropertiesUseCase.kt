package com.nguyenminhkhang.taskmanagement.domain.usecase

import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import javax.inject.Inject


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

class UpdateTaskPropertiesUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
}