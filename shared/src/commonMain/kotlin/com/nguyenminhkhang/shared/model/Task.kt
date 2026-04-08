package com.nguyenminhkhang.shared.model

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
    val repeatDaysOfWeek: List<String> = emptyList(),
    val repeatInterval: String = "",
    val repeatEndType: String = "",
    val repeatEndDate: Long? = null,
    val repeatEndCount: Int = 1,
    val startTime: Long? = null,
    val updatedAt: Long = 0L,
    val createdAt: Long = 0L,
    val reminderTimeMillis: Long? = null
)

