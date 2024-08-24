package com.githubrepos.core.test

import com.githubrepos.core.AppDispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

class TestAppDispatchers(testDispatcher: TestDispatcher = StandardTestDispatcher()) : AppDispatchers {
    override val main = testDispatcher
    override val io = testDispatcher
    override val default = testDispatcher
}