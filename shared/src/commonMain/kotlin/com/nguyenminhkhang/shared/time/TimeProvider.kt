package com.nguyenminhkhang.shared.time

interface TimeProvider {
    fun getCurrentTimeMillis(): Long
    fun combineDateAndTime(dateMillis: Long?, hour: Int?, minute: Int?): Long?
}
