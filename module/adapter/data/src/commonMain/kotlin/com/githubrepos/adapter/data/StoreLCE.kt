package com.githubrepos.adapter.data

import com.githubrepos.core.Lce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreReadResponse

fun <T, R> Flow<StoreReadResponse<T>>.toLce(transform: (T) -> R): Flow<Lce<out R>> {
    return map {
        when (it) {
            is StoreReadResponse.Initial, is StoreReadResponse.Loading -> Lce.Loading
            is StoreReadResponse.Data -> {
                Lce.Content(transform(it.value))
            }
            is StoreReadResponse.Error -> {
                val exception = when (it) {
                    is StoreReadResponse.Error.Exception -> it.error
                    is StoreReadResponse.Error.Message -> Exception(it.message)
                    is StoreReadResponse.Error.Custom<*> -> Exception("StoreReadResponse.Error.Custom")
                }
                Lce.Error(exception)
            }
            is StoreReadResponse.NoNewData -> Lce.Error(Exception("StoreReadResponse.NoNewData"))
        }
    }
}

fun <T> Flow<StoreReadResponse<T>>.toLce(): Flow<Lce<out T>> {
    return toLce { it }
}
