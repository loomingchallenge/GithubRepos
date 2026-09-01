package com.githubrepos.adapter.data.datasource.network

import com.githubrepos.adapter.data.datasource.network.service.GithubService

const val BASE_URL = "https://api.github.com/"

const val HOST_NAME_PATTERN = "*.github.com"

// Certificate #2: EC 256 bits (SHA256withECDSA)
// https://www.ssllabs.com/ssltest/analyze.html?d=api.github.com
// Valid until Fri, 27 Nov 2026 23:59:59 UTC
const val PUBLIC_KEY_PIN = "sha256/S2LUIbq4yUg5w+MYbj5LZOWAZAzaeNGJ9rTTc4GjvBQ="

interface NetworkServiceProvider {

    fun createGithubService(): GithubService
}
