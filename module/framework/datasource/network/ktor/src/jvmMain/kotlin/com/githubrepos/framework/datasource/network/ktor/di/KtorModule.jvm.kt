package com.githubrepos.framework.datasource.network.ktor.di

import com.githubrepos.adapter.data.datasource.network.HOST_NAME_PATTERN
import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.adapter.data.datasource.network.PUBLIC_KEY_PIN
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.CertificatePinner
import java.net.UnknownHostException

internal actual fun createHttpClientEngine(): HttpClientEngine {
    return OkHttp.create {
        config {
            val certificatePinner = CertificatePinner.Builder()
                .add(HOST_NAME_PATTERN, PUBLIC_KEY_PIN)
                .build()

            certificatePinner(certificatePinner)
        }
    }
}

internal actual fun interceptException(exc: Exception): Exception =
    if (exc is UnknownHostException) {
        NoInternetException()
    } else {
        exc
    }