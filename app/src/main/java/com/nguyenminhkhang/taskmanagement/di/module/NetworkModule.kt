package com.nguyenminhkhang.taskmanagement.di.module

import com.nguyenminhkhang.shared.core.network.AndroidNetworkObserver
import com.nguyenminhkhang.shared.core.network.NetworkConnectivityObserver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    single<NetworkConnectivityObserver> {
        AndroidNetworkObserver(androidContext())
    }
}