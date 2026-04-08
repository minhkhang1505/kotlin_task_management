package com.nguyenminhkhang.taskmanagement.notification

import com.nguyeminhkhang.shared.model.Task


interface TaskScheduler {
    fun schedule(task: Task)
    fun scheduleDailyCheckIn(timeMillis: Long)
    fun scheduleInactiveReminder(timeMillis: Long)
    fun scheduleMissedTask(task: Task, triggerTime: Long)
    fun cancel(task : Task)
}