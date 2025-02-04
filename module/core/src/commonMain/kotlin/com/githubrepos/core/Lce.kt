package com.githubrepos.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

sealed class Lce<T> {
    data object Loading : Lce<Nothing>()
    data class Content<T>(val content: T) : Lce<T>()
    data class Error(val error: Throwable) : Lce<Nothing>()
}

fun <T> flowLce(func: suspend () -> T): Flow<Lce<out T>> = flow {
    emit(Lce.Loading)
    emit(Lce.Content(func()))
}.catch { e ->
    e.printStackTrace()
    emit(Lce.Error(e))
}

fun <T1, T2, R> combineLCE(
    flow1: Flow<Lce<out T1>>,
    flow2: Flow<Lce<out T2>>,
    transform: suspend (a: T1, b: T2) -> R
): Flow<Lce<out R>> {
    return flow1.combine(flow2) { lce1: Lce<out T1>, lce2: Lce<out T2> ->
        if (lce1 is Lce.Error) {
            lce1
        } else if (lce2 is Lce.Error) {
            lce2
        } else if (lce1 is Lce.Loading || lce2 is Lce.Loading) {
            Lce.Loading
        } else {
            val a = (lce1 as Lce.Content<out T1>).content
            val b = (lce2 as Lce.Content<out T2>).content
            Lce.Content(transform(a, b))
        }
    }
}

class LceFlows<T> {
    val loading = MutableStateFlow(false)
    val content = MutableStateFlow<T?>(null)
    val error = MutableStateFlow<Throwable?>(null)

    fun clearError() {
        error.value = null
    }
}

fun <T> Flow<Lce<out T>>.saveToLceFlows(lceFlows: LceFlows<T>): Flow<Lce<out T>> {
    return onEach { lce ->
        when (lce) {
            is Lce.Loading -> lceFlows.loading.value = true
            is Lce.Content -> {
                lceFlows.content.value = lce.content
                lceFlows.loading.value = false
            }
            is Lce.Error -> {
                lceFlows.error.value = lce.error
                lceFlows.loading.value = false
            }
        }
    }
}
