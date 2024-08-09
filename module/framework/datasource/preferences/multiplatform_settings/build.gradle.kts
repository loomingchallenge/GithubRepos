import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
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
            baseName = "multiplatform_settings"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":module:adapter:data"))
            implementation(libs.multiplatform.settings)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        androidMain.dependencies {
            implementation(libs.androidx.security)
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.androidx.test.core)
                implementation(libs.androidx.test.junit)
                implementation(libs.robolectric)
            }
        }
        iosMain.dependencies {

        }
        jvmMain.dependencies {

        }
    }
}

android {
    namespace = "com.githubrepos.framework.datasource.preferences.multiplatform_settings"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// https://slack-chats.kotlinlang.org/t/16295408/is-it-possible-to-somehow-access-the-ios-keychain-in-kotlin-
// Hacks to get KeychainSettingsTest running on iOS simulator
// https://youtrack.jetbrains.com/issue/KT-61470
kotlin {
    iosSimulatorArm64 {
        testRuns.configureEach {
            executionSource.binary.linkerOpts(
                "-sectcreate",
                "__TEXT",
                "__entitlements",
                file("$projectDir/src/iosTest/resources/entitlements.plist").absolutePath
            )
        }
    }
}
if ("mac" in System.getProperties()["os.name"].toString().lowercase()) {
    tasks.create<Exec>("launchIosSimulator") {
        commandLine("open", "-a", "Simulator")
    }

    tasks.getByName("iosSimulatorArm64Test").dependsOn("launchIosSimulator")
}
tasks.withType<KotlinNativeSimulatorTest>().getByName("iosSimulatorArm64Test") {
    standalone.set(false)
    device.set("booted")
}