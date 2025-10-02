package com.nguyenminhkhang.taskmanagement.domain.mapper

import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.domain.model.Task

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
    repeatDaysOfWeek = repeatDaysOfWeek,
    repeatInterval = repeatInterval,
    repeatEndType = repeatEndType,
    repeatEndDate = repeatEndDate,
    repeatEndCount = repeatEndCount,
    startTime = startTime,
    updatedAt = updatedAt,
    createdAt = createdAt,
    reminderTimeMillis = reminderTimeMillis
)

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
    repeatDaysOfWeek = repeatDaysOfWeek,
    repeatInterval = repeatInterval,
    repeatEndType = repeatEndType,
    repeatEndDate = repeatEndDate,
    repeatEndCount = repeatEndCount,
    startTime = startTime,
    updatedAt = updatedAt,
    createdAt = createdAt,
    reminderTimeMillis = reminderTimeMillis
)