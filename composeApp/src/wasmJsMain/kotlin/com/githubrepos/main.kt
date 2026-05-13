package com.githubrepos

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.githubrepos.di.KoinManager
import com.githubrepos.di.getWasmModuleFactories
import com.githubrepos.ui.UI

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    KoinManager().run {
        addModuleFactories(getWasmModuleFactories())
        startKoin()
    }
    ComposeViewport(content = { UI() })
}