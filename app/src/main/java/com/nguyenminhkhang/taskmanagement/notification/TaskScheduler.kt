package com.nguyenminhkhang.taskmanagement.notification

import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity

interface TaskScheduler {
    fun schedule(task: TaskEntity)
    fun scheduleDailyCheckIn(timeMillis: Long)
    fun scheduleInactiveReminder(timeMillis: Long)
    fun scheduleMissedTask(task: TaskEntity, triggerTime: Long)
    fun cancel(task : TaskEntity)
}