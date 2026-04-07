package com.nguyenminhkhang.taskmanagement.domain.usecase.home

import com.nguyenminhkhang.taskmanagement.core.time.TimeProvider
import javax.inject.Inject

class CombineDateAndTimeUseCase @Inject constructor(
    private val timeProvider: TimeProvider
) {
    operator fun invoke(dateMillis: Long?, hour: Int?, minute: Int?): Long? {
        return timeProvider.combineDateAndTime(dateMillis, hour, minute)
    }
}
