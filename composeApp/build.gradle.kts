import com.githubrepos.`build-logic`.isTestTask
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

object Flavor {
    const val FULL = "Full"
    const val DEMO = "Demo"
}

val flavorName = "FLAVOR"
val flavorFilePath = "tmp/flavor.properties"

tasks.register("writeFlavorProperty") {
    description = "Write flavor to the temporary flavor.properties file."
    group = "build setup"
    val flavor = project.findProperty(flavorName) as String? ?: Flavor.DEMO
    val tempPropsFile = project.rootProject.layout.buildDirectory.file(flavorFilePath).get().asFile

    doLast {
        tempPropsFile.apply {
            parentFile.mkdirs()
            writeText("$flavorName=$flavor")
            println("Wrote $flavorName=$flavor to ${this.absolutePath}")
        }
    }
}

fun Project.getIosFlavor(): String? {
    val taskRequest = gradle.startParameter.taskRequests.toString()
    return if (taskRequest.contains("Apple")) {
        val tempFile = rootProject.layout.buildDirectory.file(flavorFilePath).get().asFile
        if (tempFile.exists()) {
            val flavor = tempFile.readText().substringAfter("$flavorName=").trim()
            flavor.ifEmpty { null }
        } else {
            null
        }
    } else {
        null
    }
}

fun Project.getAndroidFlavor(): String? {
    val taskRequests = gradle.startParameter.taskRequests.toString()
    val flavorRegex = Regex("assemble([a-zA-Z]+)(Debug|Release)")
    val matchResult = flavorRegex.find(taskRequests)
    return matchResult?.groups?.get(1)?.value
}

val iosFlavor = project.getIosFlavor()
val androidFlavor = project.getAndroidFlavor()
val envFlavor: String? = System.getenv(flavorName)

val flavor = if (envFlavor == Flavor.FULL || androidFlavor == Flavor.FULL || iosFlavor == Flavor.FULL) {
    Flavor.FULL
} else if (envFlavor == Flavor.DEMO || androidFlavor == Flavor.DEMO || iosFlavor == Flavor.DEMO) {
    Flavor.DEMO
} else {
    null
}
println("Flavor: $flavor")
println("Test task: ${project.isTestTask()}")

kotlin {

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.target.get()))
        }
    }

    jvm()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            binaryOption("bundleId", "com.githubrepos.shared")
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

            if (flavor == Flavor.FULL) {
                implementation(project(":module:flavor:full"))
            } else {
                implementation(project(":module:flavor:demo"))
            }

            // Compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.components.resources)

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
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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