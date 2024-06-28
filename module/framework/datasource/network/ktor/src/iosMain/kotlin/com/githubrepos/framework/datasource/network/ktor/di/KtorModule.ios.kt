package com.githubrepos.framework.datasource.network.ktor.di

import com.githubrepos.adapter.data.datasource.network.NoInternetException
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

internal actual fun createHttpClientEngine(): HttpClientEngine {
    return Darwin.create {
    }
}

internal actual fun interceptException(exc: Exception): Exception =
    if (exc.message?.contains("The Internet connection appears to be offline.") == true) {
        NoInternetException()
    } else {
        exc
    }