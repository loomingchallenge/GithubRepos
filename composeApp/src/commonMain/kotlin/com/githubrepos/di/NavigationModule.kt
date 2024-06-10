package com.githubrepos.di

import com.githubrepos.framework.ui.navigation.di.createNavigationController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val navigationModule = module {
    singleOf(::createNavigationController)
}
