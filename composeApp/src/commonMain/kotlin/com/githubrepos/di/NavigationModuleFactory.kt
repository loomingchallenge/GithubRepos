package com.githubrepos.di

import com.githubrepos.framework.ui.navigation.di.createNavigationController
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class NavigationModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
            singleOf(::createNavigationController)
        }
    }
}