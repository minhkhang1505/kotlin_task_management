package com.nguyenminhkhang.taskmanagement.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long? = null,

    @ColumnInfo(name = "user_id")
    @SerializedName("userId")
    val userId: String = "local_user",

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val content: String = "",

    @ColumnInfo(name = "favorite")
    @SerializedName("favorite")
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "completed")
    @SerializedName("completed")
    val completed: Boolean = false,

    @ColumnInfo(name = "collection_id")
    @SerializedName("collectionId")
    val collectionId: Long = 0L,

    @ColumnInfo(name = "task_detail")
    @SerializedName("taskDetail")
    val taskDetail: String = "",

    @ColumnInfo(name = "start_date")
    @SerializedName("startDate")
    val startDate: Long? = null,

    @ColumnInfo(name = "due_date")
    @SerializedName("dueDate")
    val dueDate: Long? = null,

    @ColumnInfo(name = "reminder_time")
    @SerializedName("reminderTime")
    val reminderTime: Int = 30,

    @ColumnInfo(name = "priority")
    @SerializedName("priority")
    val priority: Int = 0,

    @ColumnInfo(name = "repeat_every")
    @SerializedName("repeatEvery")
    val repeatEvery: Long = 1L,

    @ColumnInfo(name = "repeat_days_of_week")
    @SerializedName("repeatDaysOfWeek")
    val repeatDaysOfWeek: List<String>? = null,

    @ColumnInfo(name = "repeat_interval")
    @SerializedName("repeatInterval")
    val repeatInterval: String? = null,

    @ColumnInfo(name = "repeat_end_type")
    @SerializedName("repeatEndType")
    val repeatEndType: String? = null,

    @ColumnInfo(name = "repeat_end_date")
    @SerializedName("repeatEndDate")
    val repeatEndDate: Long? = null,

    @ColumnInfo(name = "repeat_end_count")
    @SerializedName("repeatEndCount")
    val repeatEndCount: Int = 1,

    @ColumnInfo(name = "start_time")
    @SerializedName("startTime")
    val startTime: Long? = null,

    @ColumnInfo(name = "updated_at")
    @SerializedName("updatedAt")
    val updatedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "created_at")
    @SerializedName("createdAt")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "reminder_time_millis")
    @SerializedName("reminderTimeMillis")
    val reminderTimeMillis: Long? = null
) : Serializable