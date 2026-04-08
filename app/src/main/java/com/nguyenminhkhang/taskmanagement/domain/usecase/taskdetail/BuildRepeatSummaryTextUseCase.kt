package com.nguyenminhkhang.taskmanagement.domain.usecase.taskdetail

import com.nguyeminhkhang.shared.model.Task
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class BuildRepeatSummaryTextUseCase () {
    private fun formatDate(millis: Long): String {
        val localDate = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        return "${localDate.monthNumber.toString().padStart(2, '0')}/${localDate.dayOfMonth.toString().padStart(2, '0')}/${localDate.year}"
    }

    operator fun invoke(task: Task?): String {
        if (task == null) {
            return ""
        }

        val repeatInterval = task.repeatInterval
        val repeatDays = task.repeatDaysOfWeek
        if (repeatInterval.isBlank() && repeatDays.isEmpty()) {
            return ""
        }

        val repeatContent = StringBuilder()

        if (repeatInterval.isNotBlank()) {
            repeatContent.append("Once every ${task.repeatEvery} ${repeatInterval.lowercase()}")
        }
        if (repeatDays.isNotEmpty()) {
            repeatContent.append(" on ${repeatDays.joinToString(", ")}")
        }
        if (task.repeatEndType == "At") {
            task.repeatEndDate?.let { repeatEndDate ->
                repeatContent.append(", until ${formatDate(repeatEndDate)}")
            }
        }
        if (task.repeatEndType == "After") {
            repeatContent.append(", ${task.repeatEndCount} occurrences")
        }

        return repeatContent.toString()
    }
}