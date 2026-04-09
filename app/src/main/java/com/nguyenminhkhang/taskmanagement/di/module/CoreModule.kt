package com.nguyenminhkhang.taskmanagement.di.module

import com.nguyenminhkhang.shared.time.TimeProvider
import com.nguyenminhkhang.shared.time.TimeProviderImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val coreModule = module {
    single<CoroutineDispatcher> { Dispatchers.IO }
    single<TimeProvider> { TimeProviderImpl() }
}
