package com.githubrepos.framework.ui.navigation.destination

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.savedstate.read

internal object RepoDestination : Destination {
    private const val REPO_ID = "repoID"
    private const val REPO_NAME = "repoName"

    private const val ROUTE_NAME = "RepoDestination"

    override val route = "$ROUTE_NAME/{$REPO_ID}/{$REPO_NAME}"

    fun getRoute(repoID: Long, repoName: String) = "${ROUTE_NAME}/$repoID/$repoName"

    val arguments = listOf(
        navArgument(REPO_ID) { type = NavType.LongType },
        navArgument(REPO_NAME) { type = NavType.StringType }
    )

    fun getRepoID(navBackStackEntry: NavBackStackEntry) =
        navBackStackEntry.arguments?.read { getLong(REPO_ID) }

    fun getRepoName(navBackStackEntry: NavBackStackEntry) =
        navBackStackEntry.arguments?.read { getString(REPO_NAME) }
}

internal fun NavHostController.navigateToRepoDestination(repoID: Long, repoName: String) {
    this.navigate(RepoDestination.getRoute(repoID, repoName))
}