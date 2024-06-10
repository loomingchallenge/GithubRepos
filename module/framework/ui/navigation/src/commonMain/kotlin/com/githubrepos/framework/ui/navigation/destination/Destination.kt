package com.githubrepos.framework.ui.navigation.destination

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

internal interface Destination {
    val route: String
}

@Composable
internal fun NavHostController.getCurrentRoute(): String? = this
    .currentBackStackEntryAsState()
    .value?.destination?.route