package com.nguyenminhkhang.taskmanagement.ui.home.state

import com.nguyenminhkhang.shared.model.ActionMenuItem
import com.nguyenminhkhang.shared.model.SortMenuItem
import com.nguyenminhkhang.shared.model.Task
import com.nguyenminhkhang.taskmanagement.ui.common.pagertab.state.TaskGroupUiState

data class HomeUiState(
    val isShowAddNewCollectionSheetVisible : Boolean = false,
    val newTask: Task? = Task(content = ""),
    val sortMenuButtonSheet: List<SortMenuItem>? = null,
    val actionMenuButtonSheet: List<ActionMenuItem>? = null,
    val newCollectionName: String = "",
    val isNewCollectionNameDialogVisible: Boolean = false,
    var listTabGroup: List<TaskGroupUiState> = emptyList(),
    val isLoading: Boolean = false,
    val isShowAddDetailTextField: Boolean = false,
    val isAddTaskSheetVisible: Boolean = false,
    val isDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val selectedReminderHour: Int? = null,
    val selectedReminderMinute: Int? = null,
    val isShowDeleteButtonVisible: Boolean = false,

    val currentCollectionId: Long = 0L,
    val isSortDialogVisible: Boolean = false,
    val isActionBottomSheetVisible: Boolean = false
)