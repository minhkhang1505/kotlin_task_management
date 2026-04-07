package com.nguyenminhkhang.taskmanagement.domain.usecase.taskdetail

import com.nguyenminhkhang.taskmanagement.domain.model.Task
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class BuildRepeatSummaryTextUseCase @Inject constructor() {
    private fun formatDate(millis: Long): String {
        val localDate = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        return "${localDate.monthNumber.toString().padStart(2, '0')}/${localDate.dayOfMonth.toString().padStart(2, '0')}/${localDate.year}"
    }

    operator fun invoke(task: Task?): String {
        if (task == null || (task.repeatInterval == null && task.repeatDaysOfWeek.isNullOrEmpty())) {
            return ""
        }

        val repeatContent = StringBuilder()

        if (task.repeatInterval != null) {
            repeatContent.append("Once every ${task.repeatEvery} ${task.repeatInterval.lowercase()}")
        }
        if (!task.repeatDaysOfWeek.isNullOrEmpty()) {
            repeatContent.append(" on ${task.repeatDaysOfWeek.joinToString(", ")}")
        }
        if (task.repeatEndDate != null && task.repeatEndType == "At") {
            repeatContent.append(", until ${formatDate(task.repeatEndDate)}")
        }
        if (task.repeatEndType == "After") {
            repeatContent.append(", ${task.repeatEndCount} occurrences")
        }

        return repeatContent.toString()
    }
}