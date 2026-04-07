package com.githubrepos.di

import com.githubrepos.flavor.di.createApiModule
import org.koin.core.module.Module

class ApiModuleFactory(val isDebug: Boolean): ModuleFactory {
    override fun create(): Module {
        return createApiModule(isDebug)
    }
}