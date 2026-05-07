package com.nguyenminhkhang.shared.core.network

import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityObserver {
    val status: Flow<NetworkStatus>
}