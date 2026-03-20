package com.nguyenminhkhang.taskmanagement.ui.home.event

import com.nguyenminhkhang.taskmanagement.ui.common.pagertab.state.TaskUiState

sealed class TaskEvent : HomeEvent {

    object SaveNewTask : TaskEvent()
    object NewTaskCleared : TaskEvent()
    data class TaskContentChanged(val content: String) : TaskEvent()
    data class TaskDetailChanged(val detail: String) : TaskEvent()
    object ToggleNewTaskFavorite : TaskEvent()
    data class ToggleFavorite(val task: TaskUiState) : TaskEvent()
    data class ToggleComplete(val task: TaskUiState) : TaskEvent()
    data class DeleteTask(val taskId: Long) : TaskEvent()

    // Reminder
    object UndoToggleComplete : TaskEvent()
    object SelectedDateTimeCleared : TaskEvent()
    data class UpdateReminderTimeMillis(val reminder: Long) : TaskEvent()
    data class OnReminderTimeSelected(val hour: Int, val minute: Int) : TaskEvent()
    data class CombineDateAndTime(val date: Long, val hour: Int?, val minute: Int?) : TaskEvent()
}