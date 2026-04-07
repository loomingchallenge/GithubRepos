package com.githubrepos.framework.ui.test

abstract class UITest {

    open fun setUp() {
        setupMainDispatcher()
    }

    open fun tearDown() {
        tearDownMainDispatcher()
    }
}

expect fun setupMainDispatcher()

expect fun tearDownMainDispatcher()