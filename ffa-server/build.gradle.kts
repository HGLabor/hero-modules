import java.text.SimpleDateFormat
import java.util.*

plugins {
    fabric
    kotlin
    silk
    `maven-publish`
}

val minecraftVersion = "1.20.4"
version = "$minecraftVersion-1.0.10"

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

val sourceJar = tasks.register<Jar>("sourceJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "gg.norisk"
            artifactId = "ffa-server"
            version = project.version.toString()
            from(components["java"])
            artifact(sourceJar)
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = uri("https://maven.norisk.gg/repository/maven-releases/")
            val snapshotsRepoUrl = uri("https://maven.norisk.gg/repository/maven-snapshots/")
            val nexusUsername = System.getenv("NORISK_NEXUS_USERNAME") ?: project.findProperty("noriskMavenUsername") ?: ""
            val nexusPassword = System.getenv("NORISK_NEXUS_PASSWORD") ?: project.findProperty("noriskMavenPassword") ?: ""

            name = "nexus"
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = nexusUsername.toString()
                password = nexusPassword.toString()
            }
        }
    }
}



