package com.githubrepos.framework.ui.screens

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
actual fun setupMainDispatcher() {
    Dispatchers.setMain(UnconfinedTestDispatcher())
}

@OptIn(ExperimentalCoroutinesApi::class)
actual fun tearDownMainDispatcher() {
    Dispatchers.resetMain()
}