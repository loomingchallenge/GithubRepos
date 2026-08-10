plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

object Flavor {
    const val FULL = "Full"
    const val DEMO = "Demo"
}

android {
    namespace = "com.githubrepos.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.githubrepos"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    flavorDimensions.add("default")
    productFlavors {
        create(Flavor.FULL) {
            dimension = "default"
            applicationIdSuffix = ".${Flavor.FULL}"
            versionNameSuffix = "-${Flavor.FULL}"
        }
        create(Flavor.DEMO) {
            dimension = "default"
            applicationIdSuffix = ".${Flavor.DEMO}"
            versionNameSuffix = "-${Flavor.DEMO}"
        }
    }
}

dependencies {
    implementation(project(":composeApp"))
    implementation(libs.androidx.activity.compose)
}
