package com.githubrepos.framework.ui.screens.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.githubrepos.core.test.repo0
import com.githubrepos.framework.ui.screens.theme.MainTheme

// Preview with compose resource does not work in Android Studio because of bug. It could work in Fleet.
// https://github.com/JetBrains/compose-multiplatform/issues/4932
// https://youtrack.jetbrains.com/issue/FL-25048
@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
internal fun RepoPreview() {
    MainTheme {
        Repo(repo0)
    }
}

