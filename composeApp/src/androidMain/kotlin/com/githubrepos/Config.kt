package com.githubrepos

import com.githubrepos.BuildConfig

actual object Config {

    actual val isDebug: Boolean = BuildConfig.DEBUG
}