package com.nguyenminhkhang.taskmanagement.ui.home

import com.nguyenminhkhang.taskmanagement.ui.AppMenuItem
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState

sealed class HomeEvent {
    data class ToggleFavorite(val task: TaskUiState) : HomeEvent()
    data class ToggleComplete(val task: TaskUiState) : HomeEvent()

    object ShowAddNewCollectionButton : HomeEvent()
    object HideAddNewCollectionButton : HomeEvent()
    object AddNewCollectionRequested : HomeEvent()
    object NewCollectionNameCleared : HomeEvent()
    data class UpdateCollectionRequested(val collectionId: Long) : HomeEvent()
    data class CurrentCollectionId(val collectionId: Long) : HomeEvent()
    data class NewCollectionNameChanged(val name: String) : HomeEvent()

    object ShowAddTaskSheet : HomeEvent()
    object HideAddTaskSheet : HomeEvent()
    object ToggleNewTaskFavorite : HomeEvent()
    object SaveNewTask : HomeEvent()
    object NewTaskCleared : HomeEvent()
    object ResetMenuListButtonSheet : HomeEvent()
    object UndoToggleComplete : HomeEvent()
    data class TaskContentChanged(val content: String) : HomeEvent()
    data class RequestSortTasks(val collectionId: Long) : HomeEvent()
    data class RequestShowButtonSheetOption(val list: List<AppMenuItem>) : HomeEvent()

    object ShowTimePicker : HomeEvent()
    object HideTimePicker : HomeEvent()
    data class TimeSelected(val time: Long) : HomeEvent()

    object ShowDatePicker : HomeEvent()
    object HideDatePicker : HomeEvent()
    object SelectedDateTimeCleared : HomeEvent()
    data class DateSelected(val date: Long) : HomeEvent()

    object ShowAddDetailTextField : HomeEvent()
    data class TaskDetailChanged(val detail: String) : HomeEvent()
}