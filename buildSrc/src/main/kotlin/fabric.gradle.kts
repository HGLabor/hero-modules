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
    minecraft("com.mojang:minecraft:${BuildConstants.minecraftVersion}")
    mappings("net.fabricmc:yarn:${BuildConstants.minecraftVersion}+build.9")
    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.100.7+${BuildConstants.minecraftVersion}")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.11.0+kotlin.2.0.0")

    modImplementation("gg.norisk:hero-api:1.21-1.2.0")
    modImplementation("dev.kosmx.player-anim:player-animation-lib-fabric:$playerAnimatorVersion")


    modImplementation("net.silkmc:silk-commands:$silkVersion")
    modImplementation("net.silkmc:silk-core:$silkVersion")
    modImplementation("net.silkmc:silk-network:$silkVersion")
    modImplementation("net.silkmc:silk-nbt:$silkVersion")
}
