import java.text.SimpleDateFormat
import java.util.*

plugins {
    fabric
    kotlin
    silk
}

version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.wispforest.io")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    include(implementation(project(":ffa-common", configuration = "namedElements"))!!)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    modImplementation("com.sk89q.worldedit:worldedit-fabric-mc1.20.4:7.3.0") // Ändere die Versionsnummer entsprechend der gewünschten Version
}

loom {
    runConfigs.configureEach {
        this.ideConfigGenerated(true)
    }
    accessWidenerPath.set(file("src/main/resources/ffa-server.accesswidener"))
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


