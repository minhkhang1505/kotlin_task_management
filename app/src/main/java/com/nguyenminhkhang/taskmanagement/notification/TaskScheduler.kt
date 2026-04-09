package com.nguyenminhkhang.taskmanagement.notification

import com.nguyenminhkhang.shared.model.Task

interface TaskScheduler : com.nguyenminhkhang.shared.notification.TaskScheduler {
    fun scheduleDailyCheckIn(timeMillis: Long)
    fun scheduleInactiveReminder(timeMillis: Long)
    fun scheduleMissedTask(task: Task, triggerTime: Long)
    fun cancel(task : Task)
}