package com.githubrepos.framework.ui.screens.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.githubrepos.adapter.presentation.viewmodel.RepoListViewModel
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.framework.ui.screens.components.OverflowMenu
import com.githubrepos.framework.ui.screens.components.ProgressIndicator
import com.githubrepos.framework.ui.screens.components.RetryButton
import com.githubrepos.framework.ui.screens.theme.LightGray
import githubrepos.module.framework.ui.screens.generated.resources.Res
import githubrepos.module.framework.ui.screens.generated.resources.repository_list_language
import githubrepos.module.framework.ui.screens.generated.resources.repository_list_refresh
import githubrepos.module.framework.ui.screens.generated.resources.repository_list_screen_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RepoListScreen(
    repoListViewModel: RepoListViewModel,
    setTopBar: @Composable (@Composable () -> Unit) -> Unit,
    onRepoItem: (ListRepo) -> Unit
) {
    val listState = rememberLazyListState()
    val repositories = repoListViewModel.repositoryFlow.collectAsLazyPagingItems()
    val onRefresh = { repositories.refresh() }
    val onRetry = { repositories.retry() }
    setTopBar {
        RepoListTopBar(onClickRefresh = {
            onRefresh()
        })
    }

    val state = getPullRefreshState(repositories, onRefresh)
    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(state)
    ) {
        when (val loadState = repositories.loadState.refresh) {
            is LoadState.Loading -> ProgressIndicator(Modifier.align(Alignment.Center))
            is LoadState.Error -> RetryButton(Modifier.align(Alignment.Center).verticalScroll(rememberScrollState()), loadState.error, onRefresh)
            is LoadState.NotLoading -> RepositoryList(repositories, listState, onRetry = onRetry, onRepoItem)
        }
        PullRefreshIndicator(false, state, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
internal fun RepoListTopBar(onClickRefresh: () -> Unit = {}) {
    TopAppBar(
        title = @Composable { Text(text = stringResource(resource = Res.string.repository_list_screen_title)) },
        actions = @Composable {
            OverflowMenu { showMenu ->
                DropdownMenuItem(
                    content = {
                        Icon(Icons.Filled.Refresh, "refresh")
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = stringResource(resource = Res.string.repository_list_refresh))
                    },
                    onClick = {
                        showMenu.value = false
                        onClickRefresh()
                    }
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
    )
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
internal fun getPullRefreshState(repositories: LazyPagingItems<ListRepo>, onRefresh: () -> Unit): PullRefreshState {
    val state = rememberPullRefreshState(false, {
        if (repositories.loadState.refresh !is LoadState.Loading) {
            onRefresh()
        }
    })
    return state
}

@Composable
internal fun RepositoryList(repositories: LazyPagingItems<ListRepo>, listState: LazyListState, onRetry: () -> Unit = {}, onRepoItem: (ListRepo) -> Unit = {}) {
    LazyColumn(
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        loadingItem("prepend", repositories.loadState.prepend, bottomDivider = true, onClick = onRetry)
        items(
            count = repositories.itemCount,
        ) { index ->
            repositories[index]?.let {
                RepositoryItem(it, onRepoItem)
                if (index < repositories.itemCount - 1) {
                    RepositoryDivider()
                }
            }
        }
        loadingItem("append", repositories.loadState.append, topDivider = true, onClick = onRetry)
    }
}

internal fun LazyListScope.loadingItem(key: String, state: LoadState, topDivider: Boolean = false, bottomDivider: Boolean = false, onClick: () -> Unit = {}) {
    item(key) {
        if (topDivider && state !is LoadState.NotLoading) {
            RepositoryDivider()
        }
        when (state) {
            is LoadState.Loading -> ProgressIndicator()
            is LoadState.Error -> RetryButton(throwable = state.error, onClick = onClick)
            is LoadState.NotLoading -> {}
        }
        if (bottomDivider && state !is LoadState.NotLoading) {
            RepositoryDivider()
        }
    }
}

@Composable
internal fun RepositoryDivider() {
    Divider(color = LightGray, thickness = 1.dp)
}

@Composable
internal fun RepositoryItem(listRepo: ListRepo, onRepoItem: (ListRepo) -> Unit = {}) {
    Column(Modifier
        .clickable {
            onRepoItem(listRepo)
        }
        .padding(12.dp)
    ) {
        Text(
            text = listRepo.fullName,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = listRepo.description ?: ""
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(Modifier.padding(top = 12.dp)) {
            Text(
                text = stringResource(resource = Res.string.repository_list_language, listRepo.language ?: ""),
                color = MaterialTheme.colors.onBackground,
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Icon(Icons.Filled.Star, "star")
            Text(
                text = listRepo.stars.toString(),
                color = MaterialTheme.colors.onBackground,
            )
            Icon(Icons.Filled.Share, "branch", Modifier.rotate(270.0f))
            Text(
                text = listRepo.forks.toString(),
                color = MaterialTheme.colors.onBackground,
            )
        }
    }
}

