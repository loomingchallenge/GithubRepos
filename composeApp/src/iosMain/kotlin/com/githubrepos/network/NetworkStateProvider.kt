package com.githubrepos.network

import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_get_main_queue

class NetworkStateProviderImp : NetworkStateProvider {

    private val monitor = nw_path_monitor_create()
    private var isNetworkAvailable = true

    init {
        // TODO It works only the first time and then it is unreliable

//        nw_path_monitor_set_update_handler(monitor) { path ->
//            val pathStatus = nw_path_get_status(path)
//            isNetworkAvailable = pathStatus == nw_path_status_satisfied
//        }
//
//        nw_path_monitor_set_queue(monitor, dispatch_get_main_queue())
//        nw_path_monitor_start(monitor)
    }

    override fun hasNetworkConnection(): Boolean {
        return isNetworkAvailable
    }
}
