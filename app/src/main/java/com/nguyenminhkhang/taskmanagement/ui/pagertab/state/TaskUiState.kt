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
    val isCompleted: Boolean = false,
    val collectionId: Long,
    val createdAt: Long,
    val updatedAt: Long =  System.currentTimeMillis(),
    val stringUpdateAt: String,
    val repeatEvery: Long,
    val repeatDaysOfWeek: Set<String>?,
    val startDate: Long?,
    val repeatInterval: String?,
    val repeatEndType: String?,
    val repeatEndDate: Long?,
    val repeatEndCount: Int,
    val startTime: Long?,
    val taskDetail: String

)

fun TaskEntity.toTaskUiState(): TaskUiState {
    return TaskUiState(
        id = this.id,
        content = this.content,
        isFavorite = this.isFavorite,
        isCompleted = this.isCompleted,
        collectionId = this.collectionId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        stringUpdateAt = this.updatedAt.millisToDateString(),
        repeatEvery= this.repeatEvery,
        repeatDaysOfWeek= this.repeatDaysOfWeek,
        repeatInterval= this.repeatInterval,
        repeatEndType = this.repeatEndType,
        startDate = this.startDate,
        repeatEndDate = this.repeatEndDate,
        repeatEndCount = this.repeatEndCount,
        startTime = this.startTime,
        taskDetail = this.taskDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.toHourMinuteString(): String {
    return String.format("%02d:%02d", this.hour, this.minute)
}

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.toHourMinute(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, this.hour)
    calendar.set(Calendar.MINUTE, this.minute)
    return calendar.timeInMillis
}

fun String.toHourMinute(): Long {
    val parts = this.split(":")
    if (parts.size != 2) throw IllegalArgumentException("Invalid time format. Expected HH:mm")
    val hour = parts[0].toIntOrNull() ?: throw IllegalArgumentException("Invalid hour value")
    val minute = parts[1].toIntOrNull() ?: throw IllegalArgumentException("Invalid minute value")
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
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