package com.githubrepos.framework.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.githubrepos.adapter.presentation.ViewModelProvider
import com.githubrepos.framework.ui.navigation.destination.RepoDestination
import com.githubrepos.framework.ui.navigation.destination.RepoListDestination
import com.githubrepos.framework.ui.navigation.destination.getCurrentRoute
import com.githubrepos.framework.ui.navigation.destination.navigateToRepoDestination
import com.githubrepos.framework.ui.navigation.viewmodel.RepoListAndroidViewModel
import com.githubrepos.framework.ui.navigation.viewmodel.RepoAndroidViewModel
import com.githubrepos.framework.ui.screens.components.TopBarController
import com.githubrepos.adapter.presentation.error.ErrorReporter
import com.githubrepos.framework.ui.screens.navigation.NavigationController
import com.githubrepos.framework.ui.screens.screen.RepoListScreen
import com.githubrepos.framework.ui.screens.screen.RepoScreen

internal class NavigationControllerImp(
    override val errorReporter: ErrorReporter,
    override val viewModelProvider: ViewModelProvider
) : NavigationController {

    @Composable
    override fun NavHost(modifier: Modifier, topBarController: TopBarController) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = RepoListDestination.route,
            modifier = modifier
        ) {
            addRepoListScreen(topBarController, viewModelProvider, navController)
            addRepoScreen(topBarController, errorReporter, viewModelProvider, navController)
        }
    }

    private fun NavGraphBuilder.addRepoListScreen(
        topBarController: TopBarController,
        viewModelProvider: ViewModelProvider,
        navController: NavHostController
    ) {
        composable(route = RepoListDestination.route) {
            RepoListScreen(
                repoListViewModel = viewModel(initializer =  { RepoListAndroidViewModel(viewModelProvider) }).viewModel,
                setTopBar = { topBar ->
                    topBarController.setTopBar(topBar, RepoListDestination.route, navController)
                },
                onRepoItem = { repo ->
                    navController.navigateToRepoDestination(repo.id, repo.name)
                }
            )
        }
    }

    private fun NavGraphBuilder.addRepoScreen(
        topBarController: TopBarController,
        errorReporter: ErrorReporter,
        viewModelProvider: ViewModelProvider,
        navController: NavHostController
    ) {
        composable(
            route = RepoDestination.route,
            arguments = RepoDestination.arguments,
        ) {
            val repoID = RepoDestination.getRepoID(it)
            val repoName = RepoDestination.getRepoName(it)
            if (repoID != null && repoName != null) {
                RepoScreen(
                    repoViewModel = viewModel { RepoAndroidViewModel(repoID, repoName, viewModelProvider) }.viewModel,
                    errorReporter = errorReporter,
                    setTopBar = { topBar ->
                        topBarController.setTopBar(topBar, RepoDestination.route, navController)
                    },
                    onBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }

    @Composable
    private fun TopBarController.setTopBar(topBar: @Composable () -> Unit, destinationRoute: String, navController: NavHostController) {
        if (navController.getCurrentRoute() == destinationRoute) {
            this.topBar.value = topBar
        }
    }
}