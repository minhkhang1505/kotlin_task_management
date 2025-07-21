package com.nguyenminhkhang.taskmanagement.ui.home

import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState

sealed class HomeEvent {
    object ShowAddTaskSheet : HomeEvent()
    object HideAddTaskSheet : HomeEvent()
    data class TaskContentChanged(val content: String) : HomeEvent()
    data class TaskDetailChanged(val detail: String) : HomeEvent()
    object ToggleNewTaskFavorite : HomeEvent()
    object SaveNewTask : HomeEvent()
    object ShowAddDetailTextField : HomeEvent()
    object ShowDatePicker : HomeEvent()
    object HideDatePicker : HomeEvent()
    data class DateSelected(val date: Long) : HomeEvent()
    object ShowTimePicker : HomeEvent()
    object HideTimePicker : HomeEvent()
    data class TimeSelected(val time: Long) : HomeEvent()
    data class ClearSelectedDateTime(val date: Long?, val time: Long?) : HomeEvent()
    object ClearNewTask : HomeEvent()
    data class handleToggleFavorite(val task: TaskUiState) : HomeEvent()
    data class handleToggleComplete(val task: TaskUiState) : HomeEvent()
    data class requestSortTasks(val tabId: Long) : HomeEvent()
    data class requestUpdateCollection(val tabId: Long) : HomeEvent()
}