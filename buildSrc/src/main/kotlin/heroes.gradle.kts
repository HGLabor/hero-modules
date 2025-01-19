import gradle.kotlin.dsl.accessors._0d671caf434fabd71f9978bfed941bee.modImplementation
import org.gradle.kotlin.dsl.provideDelegate

plugins {
    kotlin("jvm")
    id("fabric-loom")
}

val playerAnimatorVersion: String by project

repositories {
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven {
        url = uri("https://maven.norisk.gg/repository/maven-releases/")
        credentials {
            username = (System.getenv("NORISK_NEXUS_USERNAME") ?: project.findProperty("noriskMavenUsername") ?: "").toString()
            password = (System.getenv("NORISK_NEXUS_PASSWORD") ?: project.findProperty("noriskMavenPassword") ?: "").toString()
        }
    }
    maven {
        url = uri("https://maven.norisk.gg/repository/norisk-production/")
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
    maven {
        name = "Ladysnake Mods"
        url = uri("https://maven.ladysnake.org/releases")
        content {
            includeGroup("io.github.ladysnake")
            includeGroup("org.ladysnake")
            includeGroupByRegex("dev\\.onyxstudios.*")
        }
    }
}

dependencies {
    //HEROES
    modImplementation("gg.norisk:hero-api:${BuildConstants.minecraftVersion}-1.1.34")
    modImplementation("dev.kosmx.player-anim:player-animation-lib-fabric:$playerAnimatorVersion")
    modImplementation("gg.norisk:datatracker:${BuildConstants.minecraftVersion}-1.0.8")
    modImplementation("gg.norisk:emote-lib:${BuildConstants.minecraftVersion}-1.1.11")
    modApi("io.github.ladysnake:satin:2.0.0")
    //HEROES TO TEST
    //modImplementation("gg.norisk:aang:${BuildConstants.minecraftVersion}-1.0.15")
}
