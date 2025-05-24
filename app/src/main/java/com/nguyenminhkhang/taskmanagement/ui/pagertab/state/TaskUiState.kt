package com.nguyenminhkhang.taskmanagement.ui.pagertab.state

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
    val updatedAt: Long =  System.currentTimeMillis()
)

fun TaskEntity.toTaskUiState(): TaskUiState {
    return TaskUiState(
        id = this.id,
        content = this.content,
        isFavorite = this.isFavorite,
        isCompleted = this.isCompleted,
        collectionId = this.collectionId,
        updatedAt = this.updatedAt
    )
}


fun Long.millisToDateString(): String {
    return SimpleDateFormat("EEE,dd MM yyyy", Locale.getDefault()).format(Date(this)).toString()
}