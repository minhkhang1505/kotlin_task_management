package com.nguyenminhkhang.shared.time

import kotlinx.datetime.*
import kotlin.time.Clock

@OptIn(kotlin.time.ExperimentalTime::class)
class TimeProviderImpl : TimeProvider {

    override fun getCurrentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()

    override fun combineDateAndTime(
        dateMillis: Long?,
        hour: Int?,
        minute: Int?
    ): Long? {
        if (dateMillis == null) return null

        val instant = Instant.fromEpochMilliseconds(dateMillis)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val newDateTime = if (hour != null && minute != null) {
            LocalDateTime(
                year = localDateTime.year,
                monthNumber = localDateTime.monthNumber,
                dayOfMonth = localDateTime.dayOfMonth,
                hour = hour,
                minute = minute,
                second = 0,
                nanosecond = 0
            )
        } else {
            LocalDateTime(
                year = localDateTime.year,
                monthNumber = localDateTime.monthNumber,
                dayOfMonth = localDateTime.dayOfMonth,
                hour = localDateTime.hour,
                minute = localDateTime.minute,
                second = 0,
                nanosecond = 0
            )
        }

        return newDateTime
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
    }
}