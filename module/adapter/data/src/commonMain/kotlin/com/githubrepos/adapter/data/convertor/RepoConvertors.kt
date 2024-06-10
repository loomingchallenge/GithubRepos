package com.githubrepos.adapter.data.convertor

import com.githubrepos.core.entity.Repo

fun Repo.fromNetworkToDB(): Repo {
    return this
}

fun Repo.fromDBToDomain(): Repo {
    return this
}
