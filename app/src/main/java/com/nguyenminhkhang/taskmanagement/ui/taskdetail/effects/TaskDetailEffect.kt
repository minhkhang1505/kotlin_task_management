package com.nguyenminhkhang.taskmanagement.ui.taskdetail.effects

import com.nguyenminhkhang.shared.model.Task

sealed class TaskDetailEffect {
    data class OpenCalendar(
        val task: Task,
        val startTimeMillis: Long,
        val endTimeMillis: Long
    ) : TaskDetailEffect()
}