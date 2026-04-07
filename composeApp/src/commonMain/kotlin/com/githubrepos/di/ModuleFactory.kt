package com.githubrepos.di

import org.koin.core.module.Module

interface ModuleFactory {
    fun create(): Module
}