package com.githubrepos.core.test

import com.githubrepos.core.AppDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher

class TestAppDispatchers(dispatcher: CoroutineDispatcher = StandardTestDispatcher()) : AppDispatchers {
    override val main = dispatcher
    override val io = dispatcher
    override val default = dispatcher
}