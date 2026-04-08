package com.nguyenminhkhang.taskmanagement.data.mapper

import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity
import com.nguyeminhkhang.shared.model.Task

fun TaskEntity.toDomain(): Task = Task(
    id = id,
    userId = userId,
    content = content,
    favorite = favorite,
    completed = completed,
    collectionId = collectionId,
    taskDetail = taskDetail,
    startDate = startDate,
    dueDate = dueDate,
    reminderTime = reminderTime,
    priority = priority,
    repeatEvery = repeatEvery,
    repeatDaysOfWeek = repeatDaysOfWeek.orEmpty(),
    repeatInterval = repeatInterval.orEmpty(),
    repeatEndType = repeatEndType.orEmpty(),
    repeatEndDate = repeatEndDate,
    repeatEndCount = repeatEndCount,
    startTime = startTime,
    updatedAt = updatedAt,
    createdAt = createdAt,
    reminderTimeMillis = reminderTimeMillis
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    userId = userId,
    content = content,
    favorite = favorite,
    completed = completed,
    collectionId = collectionId,
    taskDetail = taskDetail,
    startDate = startDate,
    dueDate = dueDate,
    reminderTime = reminderTime,
    priority = priority,
    repeatEvery = repeatEvery,
    repeatDaysOfWeek = repeatDaysOfWeek.takeIf { it.isNotEmpty() },
    repeatInterval = repeatInterval.takeIf { it.isNotBlank() },
    repeatEndType = repeatEndType.takeIf { it.isNotBlank() },
    repeatEndDate = repeatEndDate,
    repeatEndCount = repeatEndCount,
    startTime = startTime,
    updatedAt = updatedAt,
    createdAt = createdAt,
    reminderTimeMillis = reminderTimeMillis
)