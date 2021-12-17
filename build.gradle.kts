plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"

}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.java.dev.jna:jna:5.9.0")
    implementation("net.java.dev.jna:jna-platform:5.9.0")
    implementation("com.github.melloware:jintellitype:1.4.0")
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}