package com.githubrepos.adapter.data.datasource.network

import com.githubrepos.adapter.data.datasource.network.service.GithubService

const val BASE_URL = "https://api.github.com/"

const val HOST_NAME_PATTERN = "*.github.com"

// SHA-256 Public Key Pin
// Valid until Fri, 07 Mar 2025 23:59:59 UTC
const val PUBLIC_KEY_PIN = "sha256/lmo8/KPXoMsxI+J9hY+ibNm2r0IYChmOsF9BxD74PVc="

interface NetworkServiceProvider {

    fun createGithubService(): GithubService
}
