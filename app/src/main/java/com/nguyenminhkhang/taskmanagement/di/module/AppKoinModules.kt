package com.nguyenminhkhang.taskmanagement.di.module

import com.nguyenminhkhang.taskmanagement.di.module.analyticsModule
import com.nguyenminhkhang.taskmanagement.di.module.coreModule
import com.nguyenminhkhang.taskmanagement.di.module.databaseModule
import com.nguyenminhkhang.taskmanagement.di.module.repoModule
import com.nguyenminhkhang.taskmanagement.di.module.taskSchedulerModule
import com.nguyenminhkhang.taskmanagement.di.module.useCaseModule

val appKoinModules = listOf(
    coreModule,
    databaseModule,
    repoModule,
    analyticsModule,
    stringProviderModule,
    taskSchedulerModule,
    viewModelModule,
    useCaseModule,
    networkModule
)
