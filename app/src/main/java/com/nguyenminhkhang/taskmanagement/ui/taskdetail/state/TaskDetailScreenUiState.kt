package com.nguyenminhkhang.taskmanagement.ui.taskdetail.state

import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState

data class TaskDetailScreenUiState(
    val task: TaskUiState? = null,
    val collection: List<TaskCollection> = emptyList(),
    val currentCollection: String = "",
    val repeatSummaryText: String = "",
    val isLoading: Boolean = true,
    val isDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val isInEditMode: Boolean = false,
    val isChangeCollectionSheetVisible: Boolean = false,
)