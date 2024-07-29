package com.githubrepos.framework.datasource.network.demo_api

import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider

class NetworkStateProviderImp : NetworkStateProvider {

    var hasNetworkConnection = true

    override fun hasNetworkConnection(): Boolean {
        return hasNetworkConnection
    }
}