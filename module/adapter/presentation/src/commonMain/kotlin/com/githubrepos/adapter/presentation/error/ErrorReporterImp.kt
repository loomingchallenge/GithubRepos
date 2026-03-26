package com.githubrepos.adapter.presentation.error

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val UNKNOWN_EXCEPTION = "Unknown exception occurred."
private const val UNKNOWN_ERROR = "Unknown error occurred."

fun Error.getMessage(): String {
    return error ?: if (exc != null) {
        exc.message ?: UNKNOWN_EXCEPTION
    } else {
        UNKNOWN_ERROR
    }
}

internal class ErrorReporterImp : ErrorReporter {

    private val _error = MutableStateFlow<Error?>(null)
    override val error: StateFlow<Error?> = _error

    private val mutex = Mutex()

    override suspend fun setError(error: String?, exc: Throwable?, onErrorDismissed: (() -> Unit)?) {
        mutex.withLock {
            if (_error.value == null) {
                logError(error, exc)
                _error.value = Error(error, exc, onErrorDismissed)
            }
        }
    }

    override suspend fun clearError() {
        mutex.withLock {
            _error.value = null
        }
    }

    override suspend fun isErrorSet() =
        mutex.withLock {
            error.value != null
        }

    override fun logError(error: String?, exc: Throwable?) {
        if (error == null && exc == null) {
            println(UNKNOWN_ERROR)
        } else {
            error?.let {
                println(error)
            }
            exc?.let {
                println(it.stackTraceToString())
            }
        }
    }
}
