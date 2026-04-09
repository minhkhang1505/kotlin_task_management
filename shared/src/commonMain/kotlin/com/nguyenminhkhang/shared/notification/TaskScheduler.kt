package com.nguyenminhkhang.shared.notification

import com.nguyenminhkhang.shared.model.Task

interface TaskScheduler {
    fun schedule(task: Task)
}