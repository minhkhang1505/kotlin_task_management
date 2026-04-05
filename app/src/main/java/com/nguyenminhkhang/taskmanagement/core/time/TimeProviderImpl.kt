package com.nguyenminhkhang.taskmanagement.core.time

import javax.inject.Inject

class TimeProviderImpl @Inject constructor() : TimeProvider {
    override fun getCurrentTimeMillis(): Long = System.currentTimeMillis()
}
