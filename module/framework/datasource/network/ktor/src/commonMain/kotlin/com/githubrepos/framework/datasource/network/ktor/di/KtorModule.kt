package com.githubrepos.framework.datasource.network.ktor.di

import com.githubrepos.adapter.data.datasource.network.BASE_URL
import com.githubrepos.adapter.data.datasource.network.NetworkServiceProvider
import com.githubrepos.framework.datasource.network.ktor.NetworkServiceProviderImp
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createNetworkServiceProvider(
    areLogsEnabled: Boolean
): NetworkServiceProvider {
    return createNetworkServiceProvider(areLogsEnabled, createHttpClientEngine())
}

internal fun createNetworkServiceProvider(
    areLogsEnabled: Boolean,
    engine: HttpClientEngine
): NetworkServiceProvider {
    val client = createHttpClient(engine, areLogsEnabled)
    return NetworkServiceProviderImp(client)
}

internal fun createHttpClient(httpClientEngine: HttpClientEngine, areLogsEnabled: Boolean): HttpClient {
    val client = HttpClient(httpClientEngine) {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        if (areLogsEnabled) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY // LogLevel.ALL
            }
        }
        defaultRequest {
            url(BASE_URL)
        }
        expectSuccess = true
    }

    client.plugin(HttpSend).intercept { request ->
        try {
            execute(request)
        } catch (exc: Exception) {
            throw interceptException(exc)
        }
    }
    return client
}

internal expect fun createHttpClientEngine(): HttpClientEngine

internal expect fun interceptException(exc: Exception): Exception