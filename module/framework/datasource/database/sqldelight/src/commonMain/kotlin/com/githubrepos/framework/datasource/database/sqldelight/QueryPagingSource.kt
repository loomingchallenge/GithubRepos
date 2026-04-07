package com.githubrepos.framework.datasource.database.sqldelight

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.cash.sqldelight.Query
import app.cash.sqldelight.TransactionWithoutReturn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class QueryPagingSource<Item : Any>(
    private val context: CoroutineDispatcher,
    private val dbTransaction: (noEnclosing: Boolean, body: TransactionWithoutReturn.() -> Unit) -> Unit,
    private val countQuery: Query<Long>,
    private val getItemsQuery: (limit: Long, offset: Long) -> Query<Item>,
    private val getItemOffsetQuery: (item: Item) -> Query<Long>
) : PagingSource<Int, Item>() {

    private var anchorItem: Item? = null

    init {
        val listener = {
            invalidate()
        }
        countQuery.addListener(listener)
        registerInvalidatedCallback {
            countQuery.removeListener(listener)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        return withContext(context) {
            try {
                val key = params.key ?: 0
                val limit = when (params) {
                    is LoadParams.Prepend -> minOf(key, params.loadSize)
                    else -> params.loadSize
                }
                var totalCount = 0
                var offset = 0
                var data = listOf<Item>()
                dbTransaction(false) {
                    totalCount = countQuery.executeAsOne().toInt()
                    offset = when (params) {
                        is LoadParams.Refresh -> {
                            if (key >= totalCount - params.loadSize) maxOf(0, totalCount - params.loadSize)
                            else key
                        }
                        is LoadParams.Prepend -> maxOf(0, key - params.loadSize)
                        is LoadParams.Append -> key
                    }
                    data = getItemsQuery(limit.toLong(), offset.toLong()).executeAsList()
                }
                val nextPosToLoad = offset + data.size
                val page = LoadResult.Page(
                    data = data,
                    prevKey = offset.takeIf { it > 0 && data.isNotEmpty() },
                    nextKey = nextPosToLoad.takeIf { data.isNotEmpty() && data.size >= limit && it < totalCount },
                    itemsBefore = offset,
                    itemsAfter = maxOf(0, totalCount - nextPosToLoad),
                )
                page
            } catch (throwable: Throwable) {
                LoadResult.Error(throwable)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        val refreshKey = state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let { anchorItem ->
                this.anchorItem = anchorItem
                val anchorOffset = getItemOffsetQuery(anchorItem).executeAsOne().toInt()
                maxOf(0, anchorOffset - (state.config.initialLoadSize / 2))
            }
        }
        return refreshKey
    }
}