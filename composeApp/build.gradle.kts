import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

private val FLAVOR_FULL = "Full"
private val FLAVOR_DEMO = "Demo"

fun Project.getFlavorName(): String? {
    val taskRequests = gradle.startParameter.taskRequests.toString()
    val flavorRegex = Regex("assemble([a-zA-Z]+)(Debug|Release)")
    val matchResult = flavorRegex.find(taskRequests)
    return matchResult?.groups?.get(1)?.value
}

val androidFlavor = project.getFlavorName()

val iosFlavor: String? = System.getenv("IOS_FLAVOR")

val flavor = if (iosFlavor == FLAVOR_FULL || androidFlavor == FLAVOR_FULL) {
    FLAVOR_FULL
} else if (iosFlavor == FLAVOR_DEMO || androidFlavor == FLAVOR_DEMO) {
    FLAVOR_DEMO
} else {
    null
}
println("flavor: $flavor")

kotlin {

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = libs.versions.jvm.target.get()
            }
        }
    }

    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Modules
            implementation(project(":module:core"))
            implementation(project(":module:adapter:data"))
            implementation(project(":module:adapter:presentation"))
            implementation(project(":module:framework:datasource:database:sqldelight"))
            implementation(project(":module:framework:ui:screens"))
            implementation(project(":module:framework:ui:navigation"))

            if (flavor == FLAVOR_FULL) {
                implementation(project(":module:flavor:full"))
            } else if (flavor == FLAVOR_DEMO) {
                implementation(project(":module:flavor:demo"))
            }

            // Compose
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            // implementation(libs.koin.compose) requires old version of compose.runtime
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)

            // Koin
            implementation(libs.koin.test)
            // implementation(libs.koin.test.junit5)
        }
        androidMain.dependencies {
            // Modules
            implementation(project(":module:framework:datasource:database:sqldelight"))
            implementation(project(":module:framework:datasource:preferences:multiplatform_settings"))

            // Compose
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)

            // Koin
            implementation(libs.koin.android)
        }
        iosMain.dependencies {
            // Modules
            implementation(project(":module:framework:datasource:database:sqldelight"))
            implementation(project(":module:framework:datasource:preferences:multiplatform_settings"))
        }
        jvmMain.dependencies {
            // Modules
            implementation(project(":module:framework:datasource:database:sqldelight"))
            implementation(project(":module:framework:datasource:preferences:multiplatform_settings"))

            // Compose
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

            // Koin
            implementation(libs.koin.logger.slf4j)
            implementation(libs.logback.classic)
        }
    }
}

android {
    namespace = "com.githubrepos"
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    flavorDimensions.add("default")
    productFlavors {
        create(FLAVOR_FULL) {
            dimension = "default"
            applicationIdSuffix = ".$FLAVOR_FULL"
            versionNameSuffix = "-$FLAVOR_FULL"
            resValue("string", "app_name", "GithubRepos")
        }
        create(FLAVOR_DEMO) {
            dimension = "default"
            applicationIdSuffix = ".$FLAVOR_DEMO"
            versionNameSuffix = "-$FLAVOR_DEMO"
            resValue("string", "app_name", "$FLAVOR_DEMO-GithubRepos")
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.githubrepos"
            packageVersion = libs.versions.versionName.get()
        }
    }
}