import java.text.SimpleDateFormat
import java.util.*

plugins {
    fabric
    kotlin
    `maven-publish`
}

val mcVersion = "1.20.4"
version = "$mcVersion-1.0.0"

repositories {
    mavenCentral()
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
            artifactId = "soupcore"
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


