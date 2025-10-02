package com.githubrepos

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

object ConfigImp: Config {

    @OptIn(ExperimentalNativeApi::class)
    override val isDebug: Boolean = Platform.isDebugBinary

    override val isPullRefreshSupported = true
}