plugins {
    kotlin("jvm")
    id("fabric-loom")
}

val minecraftVersion = "1.20.4"

repositories {
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://maven.wispforest.io")
    maven("https://maven.kosmx.dev/")
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
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$minecraftVersion+build.3")
    modImplementation("net.fabricmc:fabric-loader:0.15.3")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.91.3+$minecraftVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.16+kotlin.1.9.21")
    //HERO STUFF
    modImplementation("gg.norisk:hero-api:$minecraftVersion-1.0.72-SNAPSHOT")
    modImplementation("gg.norisk:darthvader:$minecraftVersion-1.0.0")
    modImplementation("dev.kosmx.player-anim:player-animation-lib-fabric:1.0.2-rc1+1.20")
}
