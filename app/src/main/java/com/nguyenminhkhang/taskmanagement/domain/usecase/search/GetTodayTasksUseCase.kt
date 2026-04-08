package com.nguyenminhkhang.taskmanagement.domain.usecase.search

import com.nguyeminhkhang.shared.model.Task
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class GetTodayTasksUseCase (
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        val timeZone = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        val startOfDay = today.atStartOfDayIn(timeZone).toEpochMilliseconds()
        val endOfDay = today.plus(1, DateTimeUnit.DAY).atStartOfDayIn(timeZone).toEpochMilliseconds() - 1

        return taskRepository.getTodayTasks(startOfDay, endOfDay)
    }
}