package com.githubrepos.framework.datasource.network.ktor

import java.net.UnknownHostException

internal actual fun getPlatformNoInternetException(): Exception = UnknownHostException()