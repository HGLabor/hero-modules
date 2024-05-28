val kotlinVersion = "1.9.21"

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.fabricmc.net/")
}

dependencies {
    fun pluginDep(id: String, version: String) = "${id}:${id}.gradle.plugin:${version}"

    implementation(kotlin("gradle-plugin", kotlinVersion))
    implementation(kotlin("serialization", kotlinVersion))

    implementation(pluginDep("fabric-loom", "1.5-SNAPSHOT"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
}
