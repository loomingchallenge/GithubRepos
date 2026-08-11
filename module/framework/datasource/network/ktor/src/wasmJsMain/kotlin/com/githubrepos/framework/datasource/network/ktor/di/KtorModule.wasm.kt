package com.githubrepos.framework.datasource.network.ktor.di

import com.githubrepos.adapter.data.datasource.network.NoInternetException
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger

internal actual val ktorLogger: Logger = Logger.DEFAULT

internal actual fun createHttpClientEngine(): HttpClientEngine {
    return Js.create()
}

internal actual fun interceptThrowable(throwable: Throwable): Throwable =
    if (throwable.message?.contains("Fail to fetch") == true
        || throwable.message?.contains("NetworkError") == true) {
        NoInternetException()
    } else {
        throwable
    }