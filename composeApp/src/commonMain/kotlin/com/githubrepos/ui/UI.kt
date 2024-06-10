package com.githubrepos.ui

import androidx.compose.runtime.Composable
import com.githubrepos.di.KoinHelper
import com.githubrepos.framework.ui.screens.components.UI
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun UI() {
    UI(KoinHelper.navigationController)
}