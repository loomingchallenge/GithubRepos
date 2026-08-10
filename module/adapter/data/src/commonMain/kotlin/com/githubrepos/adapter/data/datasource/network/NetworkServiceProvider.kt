package com.githubrepos.adapter.data.datasource.network

import com.githubrepos.adapter.data.datasource.network.service.GithubService

const val BASE_URL = "https://api.github.com/"

const val HOST_NAME_PATTERN = "*.github.com"

// Certificate #2: EC 256 bits (SHA256withECDSA)
// https://www.ssllabs.com/ssltest/analyze.html?d=api.github.com
// Valid until Jun 3 23:59:59 2026 GMT
const val PUBLIC_KEY_PIN = "sha256/rlkAiJEjAwr5USvccZ2NlLzz7elZETOabSnkRvKdow0="

interface NetworkServiceProvider {

    fun createGithubService(): GithubService
}
