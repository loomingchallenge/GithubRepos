import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.target.get()))
        }
    }

    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "sqldelight"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":module:adapter:data"))
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.sqldelight.paging3)
        }
        commonTest.dependencies {
            implementation(project(":module:adapter:data_test"))
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }
        val androidUnitTest by getting {
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
    }
}

android {
    namespace = "com.githubrepos.framework.datasource.database.sqldelight"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

sqldelight {
    databases {
        create("DelightRepoDatabase") {
            packageName.set("com.githubrepos.framework.datasource.database.sqldelight")
        }
    }
}