package com.nguyenminhkhang.shared.usecase.home

import com.nguyenminhkhang.shared.time.TimeProvider

class CombineDateAndTimeUseCase (
    private val timeProvider: TimeProvider
) {
    operator fun invoke(dateMillis: Long?, hour: Int?, minute: Int?): Long? {
        return timeProvider.combineDateAndTime(dateMillis, hour, minute)
    }
}
