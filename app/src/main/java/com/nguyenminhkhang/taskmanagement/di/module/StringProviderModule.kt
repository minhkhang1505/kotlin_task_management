package com.nguyenminhkhang.taskmanagement.di.module

import com.nguyenminhkhang.taskmanagement.ui.common.stringprovider.AndroidStringProvider
import com.nguyenminhkhang.taskmanagement.ui.common.stringprovider.StringProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val stringProviderModule = module {
    single<StringProvider> { AndroidStringProvider(androidContext()) }
}
