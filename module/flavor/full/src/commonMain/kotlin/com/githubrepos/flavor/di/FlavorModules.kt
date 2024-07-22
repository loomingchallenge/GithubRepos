package com.githubrepos.flavor.di

fun createFlavorModules(isDebug: Boolean) = listOf(createKtorModule(isDebug))