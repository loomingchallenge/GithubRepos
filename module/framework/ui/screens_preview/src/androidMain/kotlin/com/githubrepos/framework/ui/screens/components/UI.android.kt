package com.githubrepos.framework.ui.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.githubrepos.adapter.presentation.error.Error
import com.githubrepos.adapter.presentation.error.ErrorReporter
import com.githubrepos.framework.ui.screens.theme.MainTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Preview(showBackground = true, widthDp = 300, heightDp = 600)
@Composable
internal fun ErrorPreview() {
    val errorReporter: ErrorReporter = object : ErrorReporter {

        override val error: StateFlow<Error?>
            get() = MutableStateFlow(Error(error = "Example Error"))

        override suspend fun setError(error: String?, exc: Throwable?, onErrorDismissed: (() -> Unit)?) {}

        override suspend fun clearError() {}

        override suspend fun isErrorSet(): Boolean { return true }

        override fun logError(error: String?, exc: Throwable?) {}
    }
    MainTheme {
        Error(errorReporter)
    }
}