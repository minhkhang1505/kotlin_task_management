package com.nguyenminhkhang.taskmanagement.core.time

import javax.inject.Inject
import java.util.Calendar

class TimeProviderImpl () : TimeProvider {
    override fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

    override fun combineDateAndTime(dateMillis: Long?, hour: Int?, minute: Int?): Long? {
        if (dateMillis == null) return null

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateMillis

        if (hour != null && minute != null) {
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
        }

        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }
}
