package com.githubrepos.framework.ui.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.githubrepos.framework.ui.screens.theme.MainTheme

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

// Preview with compose resources does not work in Android Studio because of bug. It could work in Fleet.
// https://github.com/JetBrains/compose-multiplatform/issues/4932
// https://youtrack.jetbrains.com/issue/FL-25048
@Preview(showBackground = true)
@Composable
internal fun RetryButtonPreview() {
    MainTheme {
        RetryButton(throwable = Throwable("Unable to resolve host \"api.github.com\": No address associated with hostname")) {
        }
    }
}
