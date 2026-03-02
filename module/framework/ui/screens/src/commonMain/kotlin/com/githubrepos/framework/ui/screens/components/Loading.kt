package com.githubrepos.framework.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.githubrepos.framework.ui.screens.theme.GrayTransparent
import com.githubrepos.framework.ui.screens.theme.LightGray
import com.githubrepos.framework.ui.screens.theme.MainTheme
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
internal fun ProgressIndicatorInBox(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    backgroundColor: Color = GrayTransparent
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {  }
            )
            .then(modifier)
    ) {
        ProgressIndicator(Modifier.align(Alignment.Center), color)
    }
}

@Composable
internal fun ProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = LightGray
) {
    CircularProgressIndicator(
        modifier = Modifier
            .testTag(LoadingTestTags.PROGRESS_INDICATOR)
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
            .testTag(LoadingTestTags.SMALL_PROGRESS_INDICATOR)
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
            .testTag(LoadingTestTags.RETRY_BUTTON)
            .padding(8.dp)
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
                    .testTag(LoadingTestTags.RETRY_BUTTON_TEXT)
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

@Preview(showBackground = true, heightDp = 100, widthDp = 100)
@Composable
internal fun ProgressIndicatorPreview() {
    MainTheme {
        ProgressIndicator()
    }
}

@Preview(showBackground = true, heightDp = 100, widthDp = 100)
@Composable
internal fun SmallProgressIndicatorPreview() {
    MainTheme {
        SmallProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
internal fun RetryButtonPreview() {
    MainTheme {
        RetryButton(throwable = Throwable("Unable to resolve host \"api.github.com\": No address associated with hostname")) {
        }
    }
}

internal object LoadingTestTags {

    private const val PREFIX = "Loading"

    const val PROGRESS_INDICATOR = "$PREFIX:ProgressIndicator"
    const val SMALL_PROGRESS_INDICATOR = "$PREFIX:SmallProgressIndicator"
    const val RETRY_BUTTON = "$PREFIX:RetryButton"
    const val RETRY_BUTTON_TEXT = "$PREFIX:RetryButtonText"
}