package com.nguyenminhkhang.taskmanagement.di.module

import com.nguyenminhkhang.taskmanagement.notification.AlarmManagerTaskScheduler
import com.nguyenminhkhang.taskmanagement.notification.TaskScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val taskSchedulerModule = module {
    single<TaskScheduler> { AlarmManagerTaskScheduler(androidContext()) }
}
