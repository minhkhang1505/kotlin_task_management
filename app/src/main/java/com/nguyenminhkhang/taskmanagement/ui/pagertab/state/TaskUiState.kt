package com.nguyenminhkhang.taskmanagement.ui.pagertab.state

import androidx.room.Update

data class TaskUiState(
    val id: Long,
    val content: String,
    val isFavorite: Boolean = false,
    val isCompleted: Boolean = false,
    val collectionId: Long,
    val updatedAt: Long,
)
