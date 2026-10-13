package com.githubrepos.framework.datasource.network.ktor.di

import com.githubrepos.adapter.data.datasource.network.HOST_NAME_PATTERN
import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.adapter.data.datasource.network.PUBLIC_KEY_PIN
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.engine.darwin.certificates.CertificatePinner
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger

internal actual val ktorLogger: Logger = Logger.DEFAULT

internal actual fun createHttpClientEngine(): HttpClientEngine {
    return Darwin.create {
//        val builder = CertificatePinner.Builder()
//            .add(HOST_NAME_PATTERN, PUBLIC_KEY_PIN)
//        handleChallenge(builder.build())
    }
}

internal actual fun interceptThrowable(throwable: Throwable): Throwable =
    if (throwable.message?.contains("The Internet connection appears to be offline.") == true) {
        NoInternetException()
    } else {
        throwable
    }