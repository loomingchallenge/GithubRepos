package com.githubrepos.framework.datasource.network.ktor

import com.githubrepos.adapter.data.datasource.network.BASE_URL
import com.githubrepos.framework.datasource.network.ktor.service.getRepoPath
import com.githubrepos.framework.datasource.network.ktor.service.getSearchReposPath
import com.goncalossilva.resources.Resource
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

class GithubServiceEngineProvider {

    var hasNetworkConnection = true

    val mockEngine: MockEngine = MockEngine { request ->

        println("request.url: ${request.url}")

        if (hasNetworkConnection) {
            val responseHeaders = headersOf(
                HttpHeaders.ContentType to listOf("application/json", "charset=UTF-8")
            )
            when(request.url.toString()) {
                "$BASE_URL${getRepoPath(31792824)}" -> {
                    respond(
                        content = Resource("src/commonTest/resources/files/repo.json").readText(),
                        status = HttpStatusCode.OK,
                        headers = responseHeaders
                    )
                }
                "$BASE_URL${getSearchReposPath()}&q=&page=1&per_page=2" -> {
                    respond(
                        content = Resource("src/commonTest/resources/files/repos.json").readText(),
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