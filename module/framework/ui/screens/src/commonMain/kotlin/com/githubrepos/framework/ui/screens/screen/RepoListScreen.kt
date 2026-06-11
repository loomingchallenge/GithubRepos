package com.githubrepos.framework.ui.screens.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.githubrepos.adapter.presentation.viewmodel.RepoListViewModel
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.framework.ui.screens.components.OverflowMenu
import com.githubrepos.framework.ui.screens.components.ProgressIndicator
import com.githubrepos.framework.ui.screens.components.RetryView
import com.githubrepos.framework.ui.screens.preview.listRepo0
import com.githubrepos.framework.ui.screens.theme.LightGray
import com.githubrepos.framework.ui.screens.theme.MainTheme
import githubrepos.module.framework.ui.screens.generated.resources.Res
import githubrepos.module.framework.ui.screens.generated.resources.repository_list_clear_search
import githubrepos.module.framework.ui.screens.generated.resources.repository_list_language
import githubrepos.module.framework.ui.screens.generated.resources.repository_list_refresh
import githubrepos.module.framework.ui.screens.generated.resources.repository_list_search
import githubrepos.module.framework.ui.screens.generated.resources.repository_list_search_hint
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
    val searchQuery by repoListViewModel.searchQuery.collectAsState()
    val isSearchActive by repoListViewModel.isSearchActive.collectAsState()

    val onRefresh = {
        repoListViewModel.clearRepositoryError()
        repositories.refresh()
    }
    val onRetry = { repositories.retry() }
    setTopBar {
        if (isSearchActive) {
            SearchTopBar(
                query = searchQuery,
                onQueryChanged = { repoListViewModel.onSearchQueryChanged(it) },
                onSearchInactive = { repoListViewModel.onSearchActiveChanged(false) }
            )
        } else {
            RepoListTopBar(
                onClickRefresh = { onRefresh() },
                onSearchClicked = { repoListViewModel.onSearchActiveChanged(true) }
            )
        }
    }

    val modifier = Modifier.fillMaxSize()
    if (repoListViewModel.isPullRefreshSupported) {
        val state = getPullRefreshState(repositories, onRefresh)
        Box(modifier.then(
                Modifier
                    .testTag(RepoListScreenTags.SCREEN)
                    .pullRefresh(state))
        ) {
            RepoListWithErrors(repositories.loadState.refresh, onRefresh, repositories, listState, onRetry, onRepoItem)
            PullRefreshIndicator(
                false,
                state,
                Modifier
                    .testTag(RepoListScreenTags.PULL_REFRESH)
                    .align(Alignment.TopCenter)
            )
        }
    } else {
        Box(modifier.testTag(RepoListScreenTags.SCREEN)) {
            RepoListWithErrors(repositories.loadState.refresh, onRefresh, repositories, listState, onRetry, onRepoItem)
        }
    }
}

@Composable
internal fun BoxScope.RepoListWithErrors(
    refreshLoadState: LoadState,
    onRefresh: () -> Unit,
    repositories: LazyPagingItems<ListRepo>,
    listState: LazyListState,
    onRetry: () -> Unit,
    onRepoItem: (ListRepo) -> Unit
) {
    when (refreshLoadState) {
        is LoadState.Loading -> ProgressIndicator(
            Modifier
                .testTag(RepoListScreenTags.PROGRESS_INDICATOR)
                .align(Alignment.Center)
        )

        is LoadState.Error -> RetryView(
            Modifier
                .align(Alignment.Center)
                .verticalScroll(
                    rememberScrollState()
                ),
            testTag = RepoListScreenTags.RETRY_VIEW,
            refreshLoadState.error,
            onRefresh
        )

        is LoadState.NotLoading -> RepositoryList(
            repositories,
            listState,
            onRetry = onRetry,
            onRepoItem
        )
    }
}

@Composable
internal fun RepoListTopBar(
    onClickRefresh: () -> Unit = {},
    onSearchClicked: () -> Unit = {}
) {
    TopAppBar(
        title = @Composable { Text(text = stringResource(resource = Res.string.repository_list_screen_title)) },
        actions = @Composable {
            IconButton(
                modifier = Modifier.testTag(RepoListScreenTags.SEARCH_BUTTON),
                onClick = onSearchClicked
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(resource = Res.string.repository_list_search)
                )
            }
            OverflowMenu(
                modifier = Modifier.testTag(RepoListScreenTags.OVERFLOW_MENU)
            ) { showMenu ->
                DropdownMenuItem(
                    modifier = Modifier.testTag(RepoListScreenTags.REFRESH_BUTTON),
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
internal fun SearchTopBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearchInactive: () -> Unit
) {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .testTag(RepoListScreenTags.SEARCH_BAR),
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colors.onPrimary.copy(alpha = 0.15f),
            elevation = 0.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                IconButton(
                    onClick = onSearchInactive,
                    modifier = Modifier.testTag(RepoListScreenTags.SEARCH_BACK_BUTTON)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(resource = Res.string.repository_list_search_hint),
                            color = MaterialTheme.colors.onPrimary.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.h6
                        )
                    }
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(RepoListScreenTags.SEARCH_TEXT_FIELD),
                        textStyle = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.onPrimary),
                        cursorBrush = SolidColor(MaterialTheme.colors.onPrimary),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { /* hide keyboard if needed */ })
                    )
                }
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = { onQueryChanged("") },
                        modifier = Modifier.testTag(RepoListScreenTags.SEARCH_CLEAR_BUTTON)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = stringResource(resource = Res.string.repository_list_clear_search),
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            }
        }
    }
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
        modifier = Modifier.testTag(RepoListScreenTags.REPOSITORY_LIST),
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        loadingItem(
            "loadingItem prepend",
            repositories.loadState.prepend,
            bottomDivider = true,
            loadingTestTag = RepoListScreenTags.PREPEND_LOADING,
            errorTestTag = RepoListScreenTags.PREPEND_ERROR,
            onClick = onRetry
        )
        items(
            count = repositories.itemCount,
            key = repositories.itemKey { it.id }
        ) { index ->
            repositories[index]?.let {
                RepositoryItem(it, onRepoItem)
                if (index < repositories.itemCount - 1) {
                    RepositoryDivider()
                }
            }
        }
        loadingItem(
            "loadingItem append",
            repositories.loadState.append,
            topDivider = true,
            loadingTestTag = RepoListScreenTags.APPEND_LOADING,
            errorTestTag = RepoListScreenTags.APPEND_ERROR,
            onClick = onRetry
        )
    }
}

internal fun LazyListScope.loadingItem(
    key: String,
    state: LoadState,
    topDivider: Boolean = false,
    bottomDivider: Boolean = false,
    loadingTestTag: String,
    errorTestTag: String,
    onClick: () -> Unit = {}
) {
    item(key) {
        if (topDivider && state !is LoadState.NotLoading) {
            RepositoryDivider()
        }
        when (state) {
            is LoadState.Loading -> ProgressIndicator(modifier = Modifier.testTag(loadingTestTag))
            is LoadState.Error -> RetryView(
                testTag = errorTestTag,
                throwable = state.error,
                onClick = onClick
            )
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
        .testTag(RepoListScreenTags.getRepositoryListItem(listRepo.id))
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
            text = listRepo.description ?: "",
            maxLines = 10
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

@Preview(showBackground = true)
@Composable
internal fun RepositoryItemPreview() {
    MainTheme {
        RepositoryItem(listRepo0)
    }
}

object RepoListScreenTags {

    private const val PREFIX = "RepoListScreen"

    const val SCREEN = "$PREFIX:Screen"

    const val PROGRESS_INDICATOR = "$PREFIX:ProgressIndicator"
    const val RETRY_VIEW = "$PREFIX:RetryView"

    const val PULL_REFRESH = "$PREFIX:PullRefresh"

    const val REPOSITORY_LIST = "$PREFIX:RepositoryList"

    const val PREPEND_LOADING = "$PREFIX:PrependLoading"
    const val PREPEND_ERROR = "$PREFIX:PrependError"

    const val APPEND_LOADING = "$PREFIX:AppendLoading"
    const val APPEND_ERROR = "$PREFIX:AppendError"

    const val OVERFLOW_MENU = "$PREFIX:OverFlowMenu"

    const val REFRESH_BUTTON = "$PREFIX:RefreshButton"

    const val SEARCH_BUTTON = "$PREFIX:SearchButton"
    const val SEARCH_BAR = "$PREFIX:SearchBar"
    const val SEARCH_BACK_BUTTON = "$PREFIX:SearchBackButton"
    const val SEARCH_TEXT_FIELD = "$PREFIX:SearchTextField"
    const val SEARCH_CLEAR_BUTTON = "$PREFIX:SearchClearButton"

    fun getRepositoryListItem(id: Long) = "$PREFIX:ListItem:$id"
}
