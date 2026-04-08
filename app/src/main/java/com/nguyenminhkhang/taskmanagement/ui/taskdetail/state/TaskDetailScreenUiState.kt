package com.nguyenminhkhang.taskmanagement.ui.taskdetail.state

import com.nguyenminhkhang.shared.model.Task
import com.nguyenminhkhang.shared.model.Collection

data class TaskDetailScreenUiState(
    val task: Task? = null,
    val collection: List<Collection> = emptyList(),
    val currentCollection: String = "",
    val repeatSummaryText: String = "",
    val isLoading: Boolean = true,
    val isDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val isInEditMode: Boolean = false,
    val isChangeCollectionSheetVisible: Boolean = false,
)