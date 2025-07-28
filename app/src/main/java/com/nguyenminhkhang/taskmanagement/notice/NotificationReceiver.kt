package com.nguyenminhkhang.taskmanagement.notice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nguyenminhkhang.taskmanagement.notice.NotificationUtils.showNotification

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra("TASK_ID", -1L)
        val taskTitle = intent.getStringExtra("TASK_TITLE") ?: "Task Reminder"

        if (taskId != -1L) {
            showNotification(context, taskId.toInt(), taskTitle, "Your task is due!")
        }
    }
}