package com.nguyenminhkhang.taskmanagement.ui.home.state

import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.ui.AppMenuItem
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState

data class HomeUiState(
    val isShowAddNewCollectionSheetVisible : Boolean = false,
    val newTask: TaskEntity? = TaskEntity(content = ""),
    val menuListButtonSheet: List<AppMenuItem>? = null,
    val newCollectionName: String = "",
    val isNewCollectionNameDialogVisible: Boolean = false,
    var listTabGroup: List<TaskGroupUiState> = emptyList(),
    val isLoading: Boolean = false,
    val isShowAddDetailTextField: Boolean = false,
    val isAddTaskSheetVisible: Boolean = false,
    val isDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val newTaskCollectionName: String = "",
    val selectedReminderHour: Int? = null,
    val selectedReminderMinute: Int? = null,
    val isShowDeleteButtonVisible: Boolean = false,
)