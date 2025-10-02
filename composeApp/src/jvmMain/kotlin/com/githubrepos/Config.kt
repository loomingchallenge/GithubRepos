package com.githubrepos

object ConfigImp: Config {

    override val isDebug: Boolean = System.getenv("DEBUG").toBoolean()

    override val isPullRefreshSupported = false
}