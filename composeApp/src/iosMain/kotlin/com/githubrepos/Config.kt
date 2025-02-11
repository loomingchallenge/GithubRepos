package com.githubrepos

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

actual object Config {

    @OptIn(ExperimentalNativeApi::class)
    actual val isDebug: Boolean = Platform.isDebugBinary

    actual val isPullRefreshSupported = true
}