package com.githubrepos.framework.ui.screens.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.githubrepos.adapter.presentation.error.ErrorReporter
import com.githubrepos.adapter.presentation.viewmodel.RepoViewModel
import com.githubrepos.core.entity.Repo
import com.githubrepos.framework.ui.screens.components.ProgressIndicator
import com.githubrepos.framework.ui.screens.components.RefreshState
import com.githubrepos.framework.ui.screens.components.RetryButton
import com.githubrepos.framework.ui.screens.components.SmallProgressIndicator
import com.githubrepos.framework.ui.screens.components.getPullRefreshState
import githubrepos.module.framework.ui.screens.generated.resources.Res
import githubrepos.module.framework.ui.screens.generated.resources.repository_list_language
import githubrepos.module.framework.ui.screens.generated.resources.repository_screen_clone
import githubrepos.module.framework.ui.screens.generated.resources.repository_screen_deployments
import githubrepos.module.framework.ui.screens.generated.resources.repository_screen_github
import githubrepos.module.framework.ui.screens.generated.resources.repository_screen_homepage
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RepoScreen(
    repoViewModel: RepoViewModel,
    errorReporter: ErrorReporter,
    setTopBar: @Composable (@Composable () -> Unit) -> Unit,
    onBack: () -> Unit
) {
    setTopBar { RepoTopBar(repoViewModel.title, onBack) }
    val isLoading = repoViewModel.repoLceFlows.loading.collectAsState().value // TODO collectAsStateWithLifecycle requires at least Compose UI 1.7.0-alpha05. https://stackoverflow.com/questions/78490378/java-lang-illegalstateexception-compositionlocal-locallifecycleowner-not-presen
    val repo = repoViewModel.repoLceFlows.content.collectAsState().value // TODO collectAsStateWithLifecycle requires at least Compose UI 1.7.0-alpha05. https://stackoverflow.com/questions/78490378/java-lang-illegalstateexception-compositionlocal-locallifecycleowner-not-presen
    val error = repoViewModel.repoLceFlows.error.collectAsState().value // TODO collectAsStateWithLifecycle requires at least Compose UI 1.7.0-alpha05. https://stackoverflow.com/questions/78490378/java-lang-illegalstateexception-compositionlocal-locallifecycleowner-not-presen

    val onRefresh = { repoViewModel.loadRepo(refresh = true) }
    val onError = { repoViewModel.repoLceFlows.clearError() }
    val refreshing = rememberSaveable { mutableStateOf(RefreshState.NOT_LOADING) }
    val state = getPullRefreshState(refreshing, isLoading, error, onRefresh)
    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(state)
    ) {
        RepoLce(isLoading, refreshing, onRefresh, repo, error, errorReporter, onError)
        PullRefreshIndicator(refreshing.value == RefreshState.LOADING, state, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
internal fun BoxScope.RepoLce(
    isLoading: Boolean,
    refreshing: MutableState<RefreshState>,
    onRefresh: () -> Unit,
    repo: Repo?,
    error: Throwable?,
    errorReporter: ErrorReporter,
    onError: () -> Unit
) {
    if (repo == null) {
        if (error != null) {
            RetryButton(Modifier.align(Alignment.Center), error, onRefresh)
        } else if (isLoading && refreshing.value == RefreshState.NOT_LOADING) {
            ProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Repo(repo = repo)
        if (isLoading && refreshing.value == RefreshState.NOT_LOADING) {
            SmallProgressIndicator(Modifier.align(Alignment.TopEnd))
        }
        error?.let {
            LaunchedEffect(it) {
                errorReporter.setError(exc = it, onErrorDismissed = onError)
            }
        }
    }
}


@Composable
internal fun RepoTopBar(title: String?, onBack: () -> Unit) {
    TopAppBar(
        title = @Composable { Text(text = title ?: "") },
        actions = @Composable { },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        navigationIcon =  {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        }
    )
}

@Composable
internal fun Repo(repo: Repo) {
    Column(
        Modifier
            .padding(12.dp)
            .verticalScroll(rememberScrollState())) {
        Text(
            text = repo.fullName,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = repo.description ?: ""
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = stringResource(resource = Res.string.repository_screen_github))
        URLText(repo.url)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = stringResource(resource = Res.string.repository_screen_homepage))
        URLText(repo.homepage)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = stringResource(resource = Res.string.repository_screen_clone))
        URLText(repo.cloneUrl)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = stringResource(resource = Res.string.repository_screen_deployments))
        URLText(repo.deploymentsUrl)
        Spacer(modifier = Modifier.height(12.dp))
        Row(Modifier.padding(top = 12.dp)) {
            Text(
                text = stringResource(resource = Res.string.repository_list_language, repo.language ?: ""),
                color = MaterialTheme.colors.onBackground,
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Icon(Icons.Filled.Star, "star")
            Text(
                text = repo.stars.toString(),
                color = MaterialTheme.colors.onBackground,
            )
            Icon(Icons.Filled.Share, "branch", Modifier.rotate(270.0f))
            Text(
                text = repo.forks.toString(),
                color = MaterialTheme.colors.onBackground,
            )
        }
    }
}

@Composable
internal fun URLText(url: String?) {
    val uriHandler = LocalUriHandler.current
    Text(
        modifier = Modifier.clickable {
            url?.let { uriHandler.openUri(url) }
        },
        text = url ?: "",
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium),
    )
}

