package com.githubrepos.network

import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider

class NetworkStateProviderImp : NetworkStateProvider {

    override fun hasNetworkConnection(): Boolean {
        return true
    }
}
