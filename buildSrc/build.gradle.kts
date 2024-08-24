plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(11)
}

repositories {
    mavenCentral()
    mavenLocal()
    google()
}

dependencies {
    implementation(gradleApi())
}