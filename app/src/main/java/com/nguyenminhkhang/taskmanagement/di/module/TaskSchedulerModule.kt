package com.nguyenminhkhang.taskmanagement.di.module

import com.nguyenminhkhang.shared.notification.TaskScheduler
import com.nguyenminhkhang.taskmanagement.notification.AlarmManagerTaskScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val taskSchedulerModule = module {
    single<TaskScheduler> { AlarmManagerTaskScheduler(androidContext()) }
}
