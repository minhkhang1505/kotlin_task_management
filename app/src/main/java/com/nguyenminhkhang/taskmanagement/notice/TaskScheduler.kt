package com.nguyenminhkhang.taskmanagement.notice

import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity

interface TaskScheduler {
    fun schedule(task: TaskEntity)
    fun cancel(task : TaskEntity)
}