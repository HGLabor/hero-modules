plugins {
    kotlin("jvm")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId =  project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
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
