package com.githubrepos.adapter.data.datasource.network

import com.githubrepos.adapter.data.datasource.network.service.GithubService

const val BASE_URL = "https://api.github.com/"

const val HOST_NAME_PATTERN = "*.github.com"

// Certificate #2: EC 256 bits (SHA256withECDSA)
// https://www.ssllabs.com/ssltest/analyze.html?d=api.github.com
// Valid until 	Sun, 05 Apr 2026 23:59:59 UTC
const val PUBLIC_KEY_PIN = "sha256/H8zmHRgw4cFDQn+MvcyfhImeWNY4kN9HXO/J9xX32gk="

interface NetworkServiceProvider {

    fun createGithubService(): GithubService
}
