import java.text.SimpleDateFormat
import java.util.*

plugins {
    kotlin("jvm")
    id("fabric-loom")
}

val mcVersion: String by project
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project
val silkVersion: String by project
val kotlinApiVersion: String by project
val geckolibVersion: String by project
val playerAnimatorVersion: String by project
val serializationVersion: String by project
val yarnVersion: String by project

repositories {
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://maven.wispforest.io")
    maven("https://maven.kosmx.dev/")
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings("net.fabricmc:yarn:$yarnVersion")
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$kotlinApiVersion")

    modImplementation("net.silkmc:silk-commands:$silkVersion")
    modImplementation("net.silkmc:silk-core:$silkVersion")
    modImplementation("net.silkmc:silk-network:$silkVersion")
    modImplementation("net.silkmc:silk-nbt:$silkVersion")
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
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs += "-Xcontext-receivers"
            freeCompilerArgs += "-Xjvm-default=all"
        }
    }
}
