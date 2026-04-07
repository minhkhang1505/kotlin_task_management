package com.nguyenminhkhang.taskmanagement.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.nguyenminhkhang.taskmanagement.domain.model.Task
import javax.inject.Inject
import java.util.Calendar

class AlarmManagerTaskScheduler @Inject constructor(
    private val context: Context,
) : TaskScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    // =========================
    // PUBLIC API
    // =========================

    override fun schedule(task: Task) {
        val reminderTime = task.reminderTimeMillis ?: return
        if (reminderTime <= System.currentTimeMillis()) return

        scheduleDeadline(task, reminderTime)

        // schedule missed task (+2h)
        val missedTime = reminderTime + 2 * 60 * 60 * 1000
        scheduleMissedTask(task, missedTime)
    }

    override fun scheduleDailyCheckIn(timeMillis: Long) {
        val triggerTime = getNextTriggerTime(timeMillis)

        val intent = buildIntent(NotificationType.DAILY_CHECKIN)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            AlarmManager.INTERVAL_DAY,
            createPendingIntent(
                NotificationType.DAILY_CHECKIN,
                intent
            )
        )
    }

    override fun scheduleInactiveReminder(timeMillis: Long) {
        val intent = buildIntent(NotificationType.INACTIVE)

        setExactAlarm(
            timeMillis,
            createPendingIntent(
                NotificationType.INACTIVE,
                intent
            )
        )
    }

    override fun scheduleMissedTask(task: Task, triggerTime: Long) {
        val intent = buildIntent(
            type = NotificationType.MISSED_TASK,
            task = task
        )

        setExactAlarm(
            triggerTime,
            createPendingIntent(
                NotificationType.MISSED_TASK,
                intent,
                task.id
            )
        )
    }

    override fun cancel(task: Task) {
        listOf(
            NotificationType.DEADLINE,
            NotificationType.MISSED_TASK
        ).forEach { type ->
            alarmManager.cancel(
                createPendingIntent(
                    type,
                    Intent(context, NotificationReceiver::class.java),
                    task.id
                )
            )
        }
    }

    // =========================
    // PRIVATE - CORE LOGIC
    // =========================

    private fun scheduleDeadline(task: Task, reminderTime: Long) {
        val intent = buildIntent(
            type = NotificationType.DEADLINE,
            task = task
        )

        setExactAlarm(
            reminderTime,
            createPendingIntent(
                NotificationType.DEADLINE,
                intent,
                task.id
            )
        )
    }

    private fun setExactAlarm(triggerTime: Long, pendingIntent: PendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

    // =========================
    // PRIVATE - HELPERS
    // =========================

    private fun buildIntent(
        type: NotificationType,
        task: Task? = null
    ): Intent {
        return Intent(context, NotificationReceiver::class.java).apply {
            putExtra("NOTIFICATION_TYPE", type.name)

            task?.let {
                putExtra("TASK_ID", it.id)
                putExtra("TASK_TITLE", it.content)
            }
        }
    }

    private fun createPendingIntent(
        type: NotificationType,
        intent: Intent,
        taskId: Long? = null
    ): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            getRequestCode(type, taskId),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getRequestCode(
        type: NotificationType,
        taskId: Long? = null
    ): Int {
        return when (type) {
            NotificationType.DAILY_CHECKIN -> 1000
            NotificationType.INACTIVE -> 2000
            NotificationType.SMART_SUGGESTION -> 3000
            NotificationType.DEADLINE -> (taskId ?: 0L).toInt()
            NotificationType.MISSED_TASK -> (taskId ?: 0L).toInt() + 100000
        }
    }

    private fun getNextTriggerTime(baseTimeMillis: Long): Long {
        val now = System.currentTimeMillis()
        return if (baseTimeMillis > now) {
            baseTimeMillis
        } else {
            baseTimeMillis + AlarmManager.INTERVAL_DAY
        }
    }
}