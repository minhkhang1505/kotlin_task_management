package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import android.content.Context
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity

sealed class TaskDetailEvent {
    data class AddToCalendar(val context: Context, val task: TaskEntity) : TaskDetailEvent()
    data class GetCurrentCollectionNameById(val collectionId: Long) : TaskDetailEvent()
    object ShowChangeCollectionSheet : TaskDetailEvent()
    object CloseChangeCollectionSheet : TaskDetailEvent()
    data class CurrentCollectionChanged(val collectionId: Long) : TaskDetailEvent()
}
