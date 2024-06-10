package com.githubrepos.di

import com.githubrepos.framework.ui.screens.navigation.NavigationController
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object KoinHelper: KoinComponent {
    val navigationController: NavigationController by inject()
}