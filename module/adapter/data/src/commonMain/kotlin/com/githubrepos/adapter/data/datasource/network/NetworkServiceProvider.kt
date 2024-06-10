package com.githubrepos.adapter.data.datasource.network

import com.githubrepos.adapter.data.datasource.network.service.GithubService

const val BASE_URL = "https://api.github.com/"

interface NetworkServiceProvider {

    fun createGithubService(): GithubService
}
