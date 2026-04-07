package com.nguyenminhkhang.taskmanagement.di.module

import com.nguyenminhkhang.taskmanagement.data.local.database.AppDb
import com.nguyenminhkhang.taskmanagement.data.local.database.dao.TaskDAO
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
	single<AppDb> { AppDb.invoke(androidContext()) }

	single<TaskDAO> { get<AppDb>().taskDao() }
}