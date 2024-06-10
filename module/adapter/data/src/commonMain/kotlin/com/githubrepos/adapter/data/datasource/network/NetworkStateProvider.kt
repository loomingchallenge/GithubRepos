package com.githubrepos.adapter.data.datasource.network

interface NetworkStateProvider {

    fun hasNetworkConnection(): Boolean
}
