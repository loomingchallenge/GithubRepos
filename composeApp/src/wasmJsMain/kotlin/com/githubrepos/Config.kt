package com.githubrepos

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => typeof __IS_DEBUG__ !== 'undefined' ? __IS_DEBUG__ : false")
private external fun isDebug(): Boolean

object ConfigImp: Config {

    override val isDebug: Boolean = isDebug()

    override val isPullRefreshSupported = false
}