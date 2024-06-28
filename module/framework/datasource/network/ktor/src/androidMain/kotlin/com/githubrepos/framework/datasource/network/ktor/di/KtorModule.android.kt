package com.githubrepos.framework.datasource.network.ktor.di

import com.githubrepos.adapter.data.datasource.network.NoInternetException
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import java.net.UnknownHostException

internal actual fun createHttpClientEngine(): HttpClientEngine {
    return OkHttp.create {
    }
}

internal actual fun interceptException(exc: Exception): Exception =
    if (exc is UnknownHostException) {
        NoInternetException()
    } else {
        exc
    }