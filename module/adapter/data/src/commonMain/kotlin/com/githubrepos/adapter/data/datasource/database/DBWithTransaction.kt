package com.githubrepos.adapter.data.datasource.database

interface DBWithTransaction {

    fun <R> withTransaction(block: () -> R): R
}