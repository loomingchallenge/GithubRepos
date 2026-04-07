package com.githubrepos.di

import org.koin.core.module.Module
import org.koin.dsl.module

class UIModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
        }
    }
}