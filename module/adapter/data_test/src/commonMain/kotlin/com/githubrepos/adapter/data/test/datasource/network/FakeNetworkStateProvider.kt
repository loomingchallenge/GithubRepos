package com.githubrepos.adapter.data.test.datasource.network

import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider

class FakeNetworkStateProvider : NetworkStateProvider {

    var hasNetworkConnection = true

    override fun hasNetworkConnection(): Boolean {
        return hasNetworkConnection
    }
}