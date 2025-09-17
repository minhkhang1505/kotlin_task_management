package com.nguyenminhkhang.taskmanagement.di

import com.nguyenminhkhang.taskmanagement.ui.common.stringprovider.AndroidStringProvider
import com.nguyenminhkhang.taskmanagement.ui.common.stringprovider.StringProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StringProviderModule {
    @Binds
    @Singleton
    abstract  fun bindStringProvider(impl: AndroidStringProvider): StringProvider
}