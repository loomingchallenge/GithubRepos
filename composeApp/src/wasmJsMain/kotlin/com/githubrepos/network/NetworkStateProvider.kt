package com.githubrepos.network

import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import web.navigator.navigator

class NetworkStateProviderImp : NetworkStateProvider {

    @OptIn(ExperimentalWasmJsInterop::class)
    override fun hasNetworkConnection(): Boolean {
        return navigator.onLine
    }
}
