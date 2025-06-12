package com.nguyenminhkhang.taskmanagement.ui.pagertab.state

import android.util.TimeUtils
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import java.text.SimpleDateFormat
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

    )
}

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.toHourMinuteString(): String {
    return String.format("%02d:%02d", this.hour, this.minute)
}

fun Long.millisToDateString(): String {
    return SimpleDateFormat("EEE,dd MM yyyy", Locale.getDefault())
        .format(Date(this)).toString()
}