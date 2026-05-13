package com.githubrepos.framework.datasource.network.ktor

import com.githubrepos.adapter.data.datasource.network.NoInternetException

internal actual fun getPlatformNoInternetException(): Exception = NoInternetException()