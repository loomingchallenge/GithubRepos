package com.githubrepos.framework.ui.navigation.destination

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument

internal object RepoDestination : Destination {
    private const val repoID = "repoID"
    private const val repoName = "repoName"

    private const val routeName = "RepoDestination"

    override val route = "$routeName/{$repoID}/{$repoName}"

    fun getRoute(repoID: Long, repoName: String) = "${routeName}/$repoID/$repoName"

    val arguments = listOf(
        navArgument(repoID) { type = NavType.LongType },
        navArgument(repoName) { type = NavType.StringType }
    )

    fun getRepoID(navBackStackEntry: NavBackStackEntry) = navBackStackEntry.arguments?.getLong(repoID)

    fun getRepoName(navBackStackEntry: NavBackStackEntry) = navBackStackEntry.arguments?.getString(repoName)
}

internal fun NavHostController.navigateToRepoDestination(repoID: Long, repoName: String) {
    this.navigate(RepoDestination.getRoute(repoID, repoName))
}