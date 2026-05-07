package com.nguyenminhkhang.shared.core.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_get_main_queue

class IOSNetworkObserver : NetworkConnectivityObserver {
    private val monitor = nw_path_monitor_create()

    override val status: Flow<NetworkStatus> = callbackFlow {
        nw_path_monitor_set_queue(monitor, dispatch_get_main_queue())

        nw_path_monitor_set_update_handler(monitor) { path ->
            val status = nw_path_get_status(path)
            if (status == nw_path_status_satisfied) {
                trySend(NetworkStatus.CONNECTED)
            } else {
                trySend(NetworkStatus.DISCONNECTED)
            }
        }

        nw_path_monitor_start(monitor)

        awaitClose {
            nw_path_monitor_cancel(monitor)
        }
    }

    fun observeNetwork(onStatusChange: (Boolean) -> Unit) {
        nw_path_monitor_set_queue(monitor, dispatch_get_main_queue())
        nw_path_monitor_set_update_handler(monitor) { path ->
            val status = nw_path_get_status(path)
            onStatusChange(status == nw_path_status_satisfied)
        }
        nw_path_monitor_start(monitor)
    }

    fun stopObserving() {
        nw_path_monitor_cancel(monitor)
    }
}
