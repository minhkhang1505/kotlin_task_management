package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import android.content.Context
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity

sealed class TaskDetailEvent {
    data class AddToCalendar(val context: Context, val task: TaskEntity) : TaskDetailEvent()
}
