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
val worldEditVersion: String by project

repositories {
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://maven.wispforest.io")
    maven("https://maven.kosmx.dev/")
    maven("https://maven.enginehub.org/repo/")
    maven {
        url = uri("https://maven.norisk.gg/repository/norisk-production/")
        credentials {
            username = (System.getenv("NORISK_NEXUS_USERNAME") ?: project.findProperty("noriskMavenUsername") ?: "").toString()
            password = (System.getenv("NORISK_NEXUS_PASSWORD") ?: project.findProperty("noriskMavenPassword") ?: "").toString()
        }
    }
    maven {
        url = uri("https://maven.norisk.gg/repository/maven-releases/")
        credentials {
            username = (System.getenv("NORISK_NEXUS_USERNAME") ?: project.findProperty("noriskMavenUsername") ?: "").toString()
            password = (System.getenv("NORISK_NEXUS_PASSWORD") ?: project.findProperty("noriskMavenPassword") ?: "").toString()
        }
    }
    maven {
        url = uri("https://maven.norisk.gg/repository/maven-snapshots/")
        credentials {
            username = (System.getenv("NORISK_NEXUS_USERNAME") ?: project.findProperty("noriskMavenUsername") ?: "").toString()
            password = (System.getenv("NORISK_NEXUS_PASSWORD") ?: project.findProperty("noriskMavenPassword") ?: "").toString()
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings("net.fabricmc:yarn:$yarnVersion")
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$kotlinApiVersion")
    //HERO STUFF
    modImplementation("gg.norisk:hero-api:1.21-1.2.0")
    //modImplementation("gg.norisk:darthvader:$mcVersion-1.1.9")
    modImplementation("dev.kosmx.player-anim:player-animation-lib-fabric:$playerAnimatorVersion")

    modImplementation("net.silkmc:silk-commands:$silkVersion")
    modImplementation("net.silkmc:silk-core:$silkVersion")
    modImplementation("net.silkmc:silk-network:$silkVersion")
    //WORLEDIT
    modCompileOnly("com.sk89q.worldedit:worldedit-fabric-mc:$worldEditVersion") // Ändere die Versionsnummer entsprechend der gewünschten Version
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

