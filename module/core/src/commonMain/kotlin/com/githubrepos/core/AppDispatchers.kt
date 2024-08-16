package com.githubrepos.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

interface AppDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

class DefaultAppDispatchers : AppDispatchers {
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
    override val default = Dispatchers.Default
}

// TODO Create shareable test module because commonTest is not shareable between modules.
class TestAppDispatchers(val testDispatcher: TestDispatcher = StandardTestDispatcher()) : AppDispatchers {
    override val main = testDispatcher
    override val io = testDispatcher
    override val default = testDispatcher
}