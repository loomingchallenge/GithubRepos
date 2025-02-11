package com.githubrepos

expect object Config {

    val isDebug: Boolean

    val isPullRefreshSupported: Boolean
}