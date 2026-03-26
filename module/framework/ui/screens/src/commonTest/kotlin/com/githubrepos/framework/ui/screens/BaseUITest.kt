package com.githubrepos.framework.ui.screens

abstract class BaseUITest {

    fun baseSetUp() {
        setupMainDispatcher()
    }

    fun baseTearDown() {
        tearDownMainDispatcher()
    }
}

expect fun setupMainDispatcher()

expect fun tearDownMainDispatcher()