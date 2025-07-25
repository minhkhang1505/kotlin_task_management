package com.nguyenminhkhang.taskmanagement.notice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class AlarmManagerTaskScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) : TaskScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(task: TaskEntity) {
        val reminderTime = task.reminderTimeMillis

        if (reminderTime == null || reminderTime <= System.currentTimeMillis()) return

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("TASK_ID",task.id)
            putExtra("TASK_TITLE", task.content)
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            reminderTime,
            PendingIntent.getBroadcast(
                context,
                task.id!!.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(task: TaskEntity) {
        val intent = Intent(context, NotificationReceiver::class.java)
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                task.id!!.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}