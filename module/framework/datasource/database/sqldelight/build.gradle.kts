@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.sqldelight)
}

kotlin {
    android {
        namespace = "com.githubrepos.framework.datasource.database.sqldelight"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.target.get()))
        }
        withHostTest { }
    }

    jvm()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "sqldelight"
            isStatic = true
        }
    }

    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":module:adapter:data"))
            implementation(libs.sqldelight.coroutines.extensions)
        }
        commonTest.dependencies {
            implementation(project(":module:adapter:data_test"))
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }
        val androidHostTest by getting {
            dependencies {
                implementation(libs.androidx.test.core)
                implementation(libs.robolectric)
            }
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
            implementation(libs.touchlab.stately.common)
            implementation(libs.touchlab.stately.isolate)
            implementation(libs.touchlab.stately.iso.collections)
        }
        jvmMain.dependencies {
            implementation(libs.sqldelight.sqlite.driver)
            implementation(libs.logback.classic)
        }
        wasmJsMain.dependencies {
            implementation(libs.sqldelight.web.worker.driver)
        }
    }
}


sqldelight {
    databases {
        create("DelightRepoDatabase") {
            packageName.set("com.githubrepos.framework.datasource.database.sqldelight")
            generateAsync.set(true)
        }
    }
}