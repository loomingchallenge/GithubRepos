package com.githubrepos.framework.datasource.network.ktor

internal actual fun getPlatformNoInternetException(): Exception = Exception("The Internet connection appears to be offline.")