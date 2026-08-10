@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        namespace = "com.githubrepos.framework.ui.screens"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.target.get()))
        }
        withDeviceTest {

        }
        androidResources.enable = true
    }

    jvm()
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "screens"
            isStatic = true
        }
    }

    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            // Modules
            api(project(":module:adapter:presentation"))

            // Compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // Paging
            implementation(libs.androidx.paging.compose)
        }
        commonTest.dependencies {
            implementation(project(":module:adapter:data_test"))
            implementation(project(":module:framework:ui:ui_test"))

            implementation(libs.kotlin.test)
            implementation(libs.compose.ui.test)
            implementation(libs.androidx.paging.testing)
        }
        jvmTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling)
            implementation(libs.androidx.compose.ui.test.manifest)
        }
        val androidDeviceTest by getting {
            dependsOn(commonTest.get())
            dependencies {
                implementation(libs.androidx.test.runner)
                implementation(libs.androidx.compose.ui.test.junit4.android)
            }
        }
    }
}

//android { TODO doriesit
//    testOptions {
//        unitTests.all {
//            it.enabled = false
//        }
//    }
//}
