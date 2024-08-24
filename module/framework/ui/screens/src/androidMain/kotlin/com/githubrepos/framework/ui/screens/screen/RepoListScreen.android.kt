package com.githubrepos.framework.ui.screens.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.githubrepos.core.test.listRepo0
import com.githubrepos.framework.ui.screens.theme.MainTheme

// Preview with compose resource does not work in Android Studio because of bug. It could work in Fleet.
// https://github.com/JetBrains/compose-multiplatform/issues/4932
// https://youtrack.jetbrains.com/issue/FL-25048
@Preview(showBackground = true)
@Composable
internal fun RepositoryItemPreview() {
    MainTheme {
        RepositoryItem(listRepo0)
    }
}