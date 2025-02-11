package com.githubrepos.adapter.data.datasource.network

import com.githubrepos.adapter.data.datasource.network.service.GithubService

const val BASE_URL = "https://api.github.com/"

const val HOST_NAME_PATTERN = "*.github.com"

// EC 256 bits (SHA256withECDSA)
// https://www.ssllabs.com/ssltest/analyze.html?d=api.github.com
// Valid until 	Thu, 05 Feb 2026 23:59:59 UTC
const val PUBLIC_KEY_PIN = "sha256/1EkvzibgiE3k+xdsv+7UU5vhV8kdFCQiUiFdMX5Guuk="

interface NetworkServiceProvider {

    fun createGithubService(): GithubService
}
