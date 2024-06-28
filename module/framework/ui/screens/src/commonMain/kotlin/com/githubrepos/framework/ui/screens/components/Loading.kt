package com.githubrepos.framework.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.githubrepos.framework.ui.screens.theme.LightGray
import githubrepos.module.framework.ui.screens.generated.resources.Res
import githubrepos.module.framework.ui.screens.generated.resources.repository_list_retry
import githubrepos.module.framework.ui.screens.generated.resources.unknown_error
import org.jetbrains.compose.resources.stringResource

internal enum class RefreshState {
    START,
    LOADING,
    NOT_LOADING;
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
internal fun getPullRefreshState(refreshing: MutableState<RefreshState>, isLoading: Boolean, error: Throwable?, onRefresh: () -> Unit): PullRefreshState {
    val state = rememberPullRefreshState(refreshing.value == RefreshState.LOADING, {
        if (!isLoading) {
            refreshing.value = RefreshState.START
            onRefresh()
        }
    })
    if (refreshing.value == RefreshState.START && isLoading) {
        refreshing.value = RefreshState.LOADING
    }
    if ((refreshing.value == RefreshState.START && error != null) ||
        (refreshing.value == RefreshState.LOADING && !isLoading)) {
        refreshing.value = RefreshState.NOT_LOADING
    }
    return state
}

@Composable
internal fun ProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = LightGray
) {
    CircularProgressIndicator(
        modifier = Modifier
            .then(modifier),
        color = color
    )
}

@Composable
internal fun SmallProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = LightGray
) {
    CircularProgressIndicator(
        modifier = Modifier
            .padding(8.dp)
            .size(20.dp)
            .then(modifier),
        color = color,
        strokeWidth = 2.dp
    )
}

@Composable
internal fun RetryButton(modifier: Modifier = Modifier, throwable: Throwable, onClick: () -> Unit) {
    Column(
        Modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
            .then(modifier)) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colors.error,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = throwable.message ?: stringResource(resource = Res.string.unknown_error),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colors.onError
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick
        ) {
            Text(text = stringResource(resource = Res.string.repository_list_retry))
        }
    }
}
