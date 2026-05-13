package com.githubrepos.adapter.data.datasource.database

interface BaseDatabase {

    suspend fun init()

    suspend fun transaction(body: suspend BaseDatabase.() -> Unit)

    fun close()
}