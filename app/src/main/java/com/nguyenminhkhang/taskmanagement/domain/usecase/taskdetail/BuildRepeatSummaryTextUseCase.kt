package com.nguyenminhkhang.taskmanagement.domain.usecase.taskdetail

import com.nguyenminhkhang.taskmanagement.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class BuildRepeatSummaryTextUseCase @Inject constructor() {
    private val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    private fun formatDate(millis: Long): String = dateFormatter.format(Date(millis))

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