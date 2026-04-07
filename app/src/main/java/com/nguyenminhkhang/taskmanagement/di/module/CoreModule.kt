package com.nguyenminhkhang.taskmanagement.di.module

import com.nguyenminhkhang.taskmanagement.core.time.TimeProvider
import com.nguyenminhkhang.taskmanagement.core.time.TimeProviderImpl
import org.koin.dsl.module

val coreModule = module {
    single<TimeProvider> { TimeProviderImpl() }
}
