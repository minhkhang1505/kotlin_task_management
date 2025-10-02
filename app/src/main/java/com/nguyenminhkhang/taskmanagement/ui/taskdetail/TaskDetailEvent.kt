package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import android.content.Context
import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity

sealed class TaskDetailEvent {
    data class AddToCalendar(val context: Context, val task: TaskEntity) : TaskDetailEvent()
    data class GetCurrentCollectionNameById(val collectionId: Long) : TaskDetailEvent()
    object ShowChangeCollectionSheet : TaskDetailEvent()
    object CloseChangeCollectionSheet : TaskDetailEvent()
    data class CurrentCollectionChanged(val collectionId: Long) : TaskDetailEvent()

    object ToggleFavorite : TaskDetailEvent()

    data class OnTitleChanged(val newTitle: String) : TaskDetailEvent()
    object SaveTitle : TaskDetailEvent()
    object OnEnterEditMode : TaskDetailEvent()
    object OnExitEditMode : TaskDetailEvent()

    data class OnDetailChange(val contentDetail: String) : TaskDetailEvent()
    object SaveDetail : TaskDetailEvent()

    object OnShowDatePicker : TaskDetailEvent()
    object OnDismissDatePicker : TaskDetailEvent()
    object OnClearDateSelected : TaskDetailEvent()
    data class OnDateSelected(val dataMillis: Long) : TaskDetailEvent()

    object OnShowTimePicker : TaskDetailEvent()
    object OnClearTimeSelected : TaskDetailEvent()
    data class OnTimeSelected(val timeMillis: Long) : TaskDetailEvent()
    object OnDismissTimePicker : TaskDetailEvent()

    object OnMarkAsDoneClicked : TaskDetailEvent()
}
