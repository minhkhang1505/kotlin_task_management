package com.nguyenminhkhang.taskmanagement.domain.model

data class Task(
    val id: Long? = null,
    val userId: String = "local_user",
    val content: String = "",
    val favorite: Boolean = false,
    val completed: Boolean = false,
    val collectionId: Long = 0L,
    val taskDetail: String = "",
    val startDate: Long? = null,
    val dueDate: Long? = null,
    val reminderTime: Int = 30,
    val priority: Int = 0,
    val repeatEvery: Long = 1L,
    val repeatDaysOfWeek: List<String>? = null,
    val repeatInterval: String? = null,
    val repeatEndType: String? = null,
    val repeatEndDate: Long? = null,
    val repeatEndCount: Int = 1,
    val startTime: Long? = null,
    val updatedAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val reminderTimeMillis: Long? = null
)

