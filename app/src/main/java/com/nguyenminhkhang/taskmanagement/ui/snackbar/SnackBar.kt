package com.nguyenminhkhang.taskmanagement.ui.snackbar

import androidx.compose.material3.SnackbarDuration

enum class SnackbarActionType {
    UNDO_TOGGLE_COMPLETE
}

data class SnackbarEvent(
    val message: String,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val actionLabel: String? = null,
    val actionType: SnackbarActionType? = null,
    val onAction: (() -> Unit)? = null
)