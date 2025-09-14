package com.nguyenminhkhang.taskmanagement.ui.pagertab.state

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class TaskUiState(
    val id: Long?,
    val content: String,
    val isFavorite: Boolean = false,
    val completed: Boolean = false,
    val collectionId: Long,
    val createdAt: Long,
    val updatedAt: Long =  System.currentTimeMillis(),
    val stringUpdateAt: String,
    val repeatEvery: Long,
    val repeatDaysOfWeek: List<String>?,
    var startDate: Long?,
    val repeatInterval: String?,
    val repeatEndType: String?,
    val repeatEndDate: Long?,
    val repeatEndCount: Int,
    val startTime: Long?,
    val taskDetail: String,
    val reminderTimeMillis: Long?
)

fun TaskUiState.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        content = this.content,
        isFavorite = this.isFavorite,
        completed = this.completed,
        collectionId = this.collectionId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        repeatEvery = this.repeatEvery,
        repeatDaysOfWeek = this.repeatDaysOfWeek,
        startDate = this.startDate,
        repeatInterval = this.repeatInterval,
        repeatEndType = this.repeatEndType,
        repeatEndDate = this.repeatEndDate,
        repeatEndCount = this.repeatEndCount,
        startTime = this.startTime,
        taskDetail = this.taskDetail,
        reminderTimeMillis = this.reminderTimeMillis
    )
}

fun TaskEntity.toTaskUiState(): TaskUiState {
    return TaskUiState(
        id = this.id,
        content = this.content,
        isFavorite = this.isFavorite,
        completed = this.completed,
        collectionId = this.collectionId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        stringUpdateAt = this.updatedAt.millisToDateString(),
        repeatEvery = this.repeatEvery,
        repeatDaysOfWeek = this.repeatDaysOfWeek,
        repeatInterval = this.repeatInterval,
        repeatEndType = this.repeatEndType,
        startDate = this.startDate,
        repeatEndDate = this.repeatEndDate,
        repeatEndCount = this.repeatEndCount,
        startTime = this.startTime,
        taskDetail = this.taskDetail,
        reminderTimeMillis = this.reminderTimeMillis
    )
}

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.toHourMinute(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, this.hour)
    calendar.set(Calendar.MINUTE, this.minute)
    return calendar.timeInMillis
}
fun Long.toHourMinuteString(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    return String.format("%02d:%02d", hour, minute)
}

fun Long.millisToDateString(): String {
    return SimpleDateFormat("EEE,dd MM yyyy", Locale.getDefault())
        .format(Date(this)).toString()
}