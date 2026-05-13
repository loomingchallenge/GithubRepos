package com.githubrepos.framework.datasource.network.ktor

import com.githubrepos.adapter.data.datasource.network.BASE_URL
import com.githubrepos.adapter.data.test.resources.repoJSON
import com.githubrepos.adapter.data.test.resources.repos1_2JSON
import com.githubrepos.framework.datasource.network.ktor.service.getRepoEndPoint
import com.githubrepos.framework.datasource.network.ktor.service.getSearchReposEndPoint
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

class GithubServiceEngineManager {

    var hasNetworkConnection = true

    val mockEngine: MockEngine = MockEngine { request ->

        println("request.url: ${request.url}")

        if (hasNetworkConnection) {
            val responseHeaders = headersOf(
                HttpHeaders.ContentType to listOf("application/json", "charset=UTF-8")
            )
            when(request.url.toString()) {
                "$BASE_URL${getRepoEndPoint(31792824)}" -> {
                    respond(
                        content = repoJSON,
                        status = HttpStatusCode.OK,
                        headers = responseHeaders
                    )
                }
                "$BASE_URL${getSearchReposEndPoint()}&q=&page=1&per_page=2" -> {
                    respond(
                        content = repos1_2JSON,
                        status = HttpStatusCode.OK,
                        headers = responseHeaders
                    )
                }
                else -> error("Unknown request: ${request.url}")
            }
        } else {
            throw getPlatformNoInternetException()
        }
    }
}

internal expect fun getPlatformNoInternetException(): Exception