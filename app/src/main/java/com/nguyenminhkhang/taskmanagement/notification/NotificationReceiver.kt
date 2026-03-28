package com.nguyenminhkhang.taskmanagement.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nguyenminhkhang.taskmanagement.notification.NotificationUtils.showNotification

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra("TASK_ID", -1L)
        val taskTitle = intent.getStringExtra("TASK_TITLE") ?: "Task Reminder"
        val type = intent.getStringExtra("NOTIFICATION_TYPE")
            ?.let { NotificationType.valueOf(it) }

        if (taskId == -1L || type == null) return

        when(type) {
            NotificationType.DEADLINE -> {
                showNotification(context, taskId.toInt(), taskTitle, "Your task is due!")
            }
            NotificationType.INACTIVE -> {
                showNotification(context, 2000, "We miss you 😢", "You have pending tasks!")
            }
            NotificationType.MISSED_TASK -> {
                showNotification(context, taskId.toInt(), taskTitle, "You missed this task!")
            }
            NotificationType.DAILY_CHECKIN -> {
                showNotification(context, 1000, "Daily Check", "Check your tasks today 👀")
            }
            NotificationType.SMART_SUGGESTION -> {
                showNotification(context, 3000, "Focus time", "Let's finish your tasks 💪")
            }
        }
    }
}