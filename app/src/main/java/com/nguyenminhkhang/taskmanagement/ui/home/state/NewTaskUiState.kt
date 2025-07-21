package com.nguyenminhkhang.taskmanagement.ui.home.state

import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState


data class NewTaskUiState(
    var listTabGroup: List<TaskGroupUiState> = emptyList(),
    val isLoading: Boolean = false,
    val isShowAddDetailTextField: Boolean = false,
    val isAddTaskSheetVisible: Boolean = false,
    val newTaskContent: String = "",
    val newTaskDetail: String = "",
    val newTaskIsFavorite: Boolean = false,

    val isDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val selectedDate: Long? = null,
    val selectedTime: Long? = null,
)