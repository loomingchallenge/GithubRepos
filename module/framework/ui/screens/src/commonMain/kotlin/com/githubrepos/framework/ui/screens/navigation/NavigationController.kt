package com.githubrepos.framework.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.githubrepos.adapter.presentation.ViewModelProvider
import com.githubrepos.framework.ui.screens.components.TopBarController
import com.githubrepos.adapter.presentation.error.ErrorReporter

interface NavigationController {

    val errorReporter: ErrorReporter

    val viewModelProvider: ViewModelProvider

    @Composable
    fun NavHost(modifier: Modifier, topBarController: TopBarController)
}