package com.nguyenminhkhang.taskmanagement.core.time

interface TimeProvider {
    fun getCurrentTimeMillis(): Long
    fun combineDateAndTime(dateMillis: Long?, hour: Int?, minute: Int?): Long?
}
