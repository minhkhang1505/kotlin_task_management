package com.nguyenminhkhang.taskmanagement.notice

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TaskSchedulerModule {

    @Binds
    abstract fun bindTaskScheduler(
        alarmManagerTaskScheduler: AlarmManagerTaskScheduler
    ): TaskScheduler
}