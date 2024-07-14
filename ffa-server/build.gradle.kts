plugins {
    fabric
    kotlin
    `maven-publish`
    kotlin("plugin.serialization")
}

version = "${property("mcVersion")}-1.1.3"

dependencies {
    include(implementation(project(":ffa-common", configuration = "namedElements"))!!)
}

loom {
    runConfigs.configureEach {
        this.ideConfigGenerated(true)
    }
    accessWidenerPath.set(file("src/main/resources/ffa-server.accesswidener"))
}

val sourceJar = tasks.register<Jar>("sourceJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        create<MavenPublication>("binary") {
            groupId = "gg.norisk"
            artifactId = "ffa-server"
            version = project.version.toString()
            from(components["java"])
        }
        create<MavenPublication>("binaryAndSources") {
            groupId = "gg.norisk"
            artifactId = "ffa-server"
            version = project.version.toString()
            from(components["java"])
            artifact(sourceJar)
        }
    }
    repositories {
        fun MavenArtifactRepository.applyCredentials() = credentials {
            username = (System.getenv("NORISK_NEXUS_USERNAME") ?: project.findProperty("noriskMavenUsername")).toString()
            password = (System.getenv("NORISK_NEXUS_PASSWORD") ?: project.findProperty("noriskMavenPassword")).toString()
        }
        maven {
            name = "production"
            url = uri("https://maven.norisk.gg/repository/norisk-production/")
            applyCredentials()
        }
        maven {
            name = "dev"
            // this could also be a maven repo on the dev server
            // e.g. maven-staging.norisk.gg
            url = uri("https://maven.norisk.gg/repository/maven-releases/")
            applyCredentials()
        }
    }
}

tasks.withType<PublishToMavenRepository>().configureEach {
    val predicate = provider {
        (repository == publishing.repositories["production"] &&
                publication == publishing.publications["binary"]) ||
                (repository == publishing.repositories["dev"] &&
                        publication == publishing.publications["binaryAndSources"])
    }
    onlyIf("publishing binary to the production repository, or binary and sources to the internal dev one") {
        predicate.get()
    }
}

tasks.withType<PublishToMavenLocal>().configureEach {
    val predicate = provider {
        publication == publishing.publications["binaryAndSources"]
    }
    onlyIf("publishing binary and sources") {
        predicate.get()
    }
}





