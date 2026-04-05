package com.nguyenminhkhang.taskmanagement.di

import com.nguyenminhkhang.taskmanagement.core.time.TimeProvider
import com.nguyenminhkhang.taskmanagement.core.time.TimeProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {
    @Binds
    @Singleton
    abstract fun bindTimeProvider(
        timeProviderImpl: TimeProviderImpl
    ): TimeProvider
}
