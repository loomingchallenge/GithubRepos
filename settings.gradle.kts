rootProject.name = "GithubRepos"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":composeApp")
include(":module:core")
include(":module:adapter:data")
include(":module:adapter:presentation")
include(":module:framework:datasource:network:demo_api")
include(":module:framework:datasource:network:ktor")
include(":module:framework:datasource:database:sqldelight")
include(":module:framework:datasource:preferences:multiplatform_settings")
include(":module:framework:ui:screens")
include(":module:framework:ui:navigation")