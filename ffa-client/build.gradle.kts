plugins {
    fabric
    kotlin
    heroes
    `maven-publish`
    kotlin("plugin.serialization")
}

version = "${BuildConstants.minecraftVersion}-1.1.8"

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
    implementation(project(":ffa-common", configuration = "namedElements"))!!
    //UI
    modCompileOnly("io.wispforest:owo-lib:0.12.10+1.21")
    include(modImplementation("com.thedeanda:lorem:2.2")!!)
    //DEBUG MOD zum einloggen
    modCompileOnly("maven.modrinth:auth-me:8.0.0+1.21")
    modCompileOnly("maven.modrinth:cloth-config:15.0.140+fabric")
    //modImplementation("maven.modrinth:replaymod:1.21.2.6.19")

    //Das hier ist hart ungeil eig sollte das ein buildskript sein aber er findet es dann nciht???
    //the mods should work with this!
    modCompileOnly("maven.modrinth:iris:1.7.3+1.21")
    modCompileOnly("maven.modrinth:sodium:mc1.21-0.5.11")
    modCompileOnly("maven.modrinth:nvidium:0.2.9-beta")
}

val sourceJar = tasks.register<Jar>("sourceJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        create<MavenPublication>("binary") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
        create<MavenPublication>("binaryAndSources") {
            groupId = project.group.toString()
            artifactId = project.name
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
