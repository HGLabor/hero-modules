import java.text.SimpleDateFormat
import java.util.*

plugins {
    fabric
    kotlin
    silk
    kotlin("plugin.serialization")
}

version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.wispforest.io")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

tasks {
    processResources {
        val properties = mapOf(
            "version" to project.version,
            "buildDate" to SimpleDateFormat("yyyyMMdd").format(Date())
        )
        inputs.properties(properties)
        filesMatching("fabric.mod.json") {
            expand(properties)
        }
    }
}
