package com.githubrepos.framework.ui.navigation.di

import com.githubrepos.adapter.presentation.ViewModelProvider
import com.githubrepos.framework.ui.navigation.NavigationControllerImp
import com.githubrepos.adapter.presentation.error.ErrorReporter
import com.githubrepos.framework.ui.screens.navigation.NavigationController

fun createNavigationController(
    errorReporter: ErrorReporter,
    viewModelProvider: ViewModelProvider
): NavigationController {
    return NavigationControllerImp(errorReporter, viewModelProvider)
}