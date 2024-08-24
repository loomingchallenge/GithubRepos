package com.githubrepos.adapter.data.test

import githubrepos.module.adapter.data_test.generated.resources.Res.readBytes
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
val repoJSON by lazy {
    runBlocking {
        readBytes("files/repo.json").decodeToString()
    }
}

@OptIn(ExperimentalResourceApi::class)
val reposJSON by lazy {
    runBlocking {
        readBytes("files/repos.json").decodeToString()
    }
}

@OptIn(ExperimentalResourceApi::class)
val repos1_2JSON by lazy {
    runBlocking {
        readBytes("files/repos1_2.json").decodeToString()
    }
}

const val repoCountInJSON = 50