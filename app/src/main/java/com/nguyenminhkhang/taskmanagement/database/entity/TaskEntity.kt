package com.nguyenminhkhang.taskmanagement.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "user_id")
    val userId: String = "",
    @ColumnInfo(name = "title")
    val content: String,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,
    @ColumnInfo(name = "collection_id")
    val collectionId: Long = 0L,

    @ColumnInfo(name = "task_detail")
    val taskDetail: String = "",


    @ColumnInfo(name = "start_date")
    val startDate: Long? = null,
    @ColumnInfo(name = "due_date")
    val dueDate: Long? = null,
    @ColumnInfo(name = "reminder_time")
    val reminderTime: Int = 30,

    @ColumnInfo(name = "priority")
    val priority: Int = 0,

    @ColumnInfo(name = "repeat_every")
    val repeatEvery: Long = 1L,
    @ColumnInfo(name = "repeat_days_of_week")
    val repeatDaysOfWeek: Set<String>? = null,
    @ColumnInfo(name = "repeat_interval")
    val repeatInterval: String? = null,
    @ColumnInfo(name = "repeat_end_type")
    val repeatEndType: String? = null,
    @ColumnInfo(name = "repeat_end_date")
    val repeatEndDate: Long? = null,
    @ColumnInfo(name = "repeat_end_count")
    val repeatEndCount: Int = 1,
    @ColumnInfo(name = "start_time")
    val startTime: Long? = null,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "reminder_time_millis")
    val reminderTimeMillis: Long? = null,
)
