package com.githubrepos

actual object Config {

    actual val isDebug: Boolean = System.getenv("DEBUG").toBoolean()

    actual val isPullRefreshSupported = false
}