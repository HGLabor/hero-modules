plugins {
    fabric
    kotlin
    `noriskclient-publish`
    kotlin("plugin.serialization")
}

version = "${property("mcVersion")}-1.0.11"

repositories {
    mavenCentral()
    maven("https://maven.wispforest.io")
    maven {
        url = uri("https://maven.norisk.gg/repository/maven-snapshots/")
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
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven")
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    include(implementation(project(":ffa-common", configuration = "namedElements"))!!)

    //UI
    modImplementation("io.wispforest:owo-lib:0.12.1-SNAPSHOT")
    modImplementation("com.thedeanda:lorem:2.2")
    //DEBUG MOD zum einloggen
    modImplementation("maven.modrinth:auth-me:8.0.0+1.20.4")
    modImplementation("maven.modrinth:sodium:mc1.20.4-0.5.8")
    modImplementation("maven.modrinth:cloth-config:13.0.121+fabric")
}
