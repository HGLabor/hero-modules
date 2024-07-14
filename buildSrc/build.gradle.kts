plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version embeddedKotlinVersion
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.fabricmc.net/")
}

dependencies {
    fun pluginDep(id: String, version: String) = "${id}:${id}.gradle.plugin:${version}"
    val kotlinVersion = "1.9.23"

    compileOnly(kotlin("gradle-plugin", embeddedKotlinVersion))
    runtimeOnly(kotlin("gradle-plugin", kotlinVersion))
    compileOnly(pluginDep("org.jetbrains.kotlin.plugin.serialization", embeddedKotlinVersion))
    runtimeOnly(pluginDep("org.jetbrains.kotlin.plugin.serialization", kotlinVersion))

    implementation(pluginDep("fabric-loom", "1.6-SNAPSHOT"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}
