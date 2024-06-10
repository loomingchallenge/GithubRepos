package com.githubrepos.adapter.presentation.error

import kotlinx.coroutines.flow.StateFlow

data class Error(
    val error: String? = null,
    val exc: Throwable? = null,
    val onErrorDismissed: (() -> Unit)? = null
)

interface ErrorReporter {

    val error: StateFlow<Error?>

    suspend fun setError(error: String? = null, exc: Throwable? = null, onErrorDismissed: (() -> Unit)? = null)

    suspend fun clearError()

    suspend fun isErrorSet(): Boolean

    fun logError(error: String? = null, exc: Throwable? = null)
}