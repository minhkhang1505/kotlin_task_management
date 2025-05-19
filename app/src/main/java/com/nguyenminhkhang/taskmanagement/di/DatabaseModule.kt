package com.nguyenminhkhang.taskmanagement.di

import android.content.Context
import com.nguyenminhkhang.taskmanagement.database.AppDb
import com.nguyenminhkhang.taskmanagement.database.dao.TaskDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun taskDao(appDb: AppDb): TaskDAO {
        return appDb.taskDao()
    }

    @Provides
    fun provideAppDb(@ApplicationContext context: Context) : AppDb { //context duoc hilt dependency inject ho tro thong qua @ApplicationContext
        return AppDb.invoke(context)
    }
}