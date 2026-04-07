package com.nguyenminhkhang.taskmanagement.di.module

val appKoinModules = listOf(
    coreModule,
    databaseModule,
    repoModule,
    analyticsModule,
    stringProviderModule,
    taskSchedulerModule,
    viewModelModule,
    useCaseModule
)
