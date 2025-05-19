package com.nguyenminhkhang.taskmanagement.di

import com.nguyenminhkhang.taskmanagement.database.dao.TaskDAO
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.repository.TaskRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Singleton //tạo ra 1 thực thể duy nhất cho toàn bộ ứng dụng
    @Provides
    fun provideTaskRepo(taskDAO: TaskDAO): TaskRepo {
        return TaskRepoImpl(taskDAO)
    }
}