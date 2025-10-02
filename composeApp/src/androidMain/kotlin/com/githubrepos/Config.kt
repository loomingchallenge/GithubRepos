package com.githubrepos

import com.githubrepos.BuildConfig

object ConfigImp: Config {

    override val isDebug: Boolean = BuildConfig.DEBUG

    override val isPullRefreshSupported = true
}