package com.githubrepos.ui

import androidx.compose.runtime.Composable
import com.githubrepos.di.KoinHelper
import com.githubrepos.framework.ui.screens.components.UI

@Composable
fun UI() {
    UI(KoinHelper.navigationController)
}