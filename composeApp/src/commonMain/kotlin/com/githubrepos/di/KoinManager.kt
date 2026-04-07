package com.githubrepos.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import kotlin.reflect.KClass

open class KoinManager {

    private val moduleFactories = mutableListOf<ModuleFactory>()

    fun addModuleFactory(factory: ModuleFactory) {
        moduleFactories.add(factory)
    }

    fun addModuleFactories(factories: List<ModuleFactory>) {
        moduleFactories.addAll(factories)
    }

    fun removeModuleFactory(factoryClass: KClass<out ModuleFactory>) {
        val wasremoved = moduleFactories.removeAll { it::class == factoryClass }
        println("KoinManager removeModuleFactory wasremoved $wasremoved")
    }

    fun replaceModuleFactory(factoryClass: KClass<out ModuleFactory>, newFactory: ModuleFactory) {
        removeModuleFactory(factoryClass)
        addModuleFactory(newFactory)
    }

    fun getModuleFactories(): List<ModuleFactory> {
        return moduleFactories
    }

    fun setModules(koinApplication: KoinApplication) {
        val modules = moduleFactories.map { it.create() }
        koinApplication.modules(modules)
    }

    open fun startKoin() {
        startKoin {
            setModules(this)
        }
    }

    open fun stopKoin() {
        org.koin.core.context.stopKoin()
    }
}