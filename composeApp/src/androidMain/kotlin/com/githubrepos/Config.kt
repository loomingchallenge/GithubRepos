package com.githubrepos

class AndroidConfig(override val isDebug: Boolean) : Config {

    override val isPullRefreshSupported = true
}