plugins {
    fabric
    kotlin
    `hglabor-publish`
    kotlin("plugin.serialization")
}

version = "${property("mcVersion")}-1.0.11"

dependencies {
    include(implementation(project(":ffa-common", configuration = "namedElements"))!!)
}

loom {
    runConfigs.configureEach {
        this.ideConfigGenerated(true)
    }
    accessWidenerPath.set(file("src/main/resources/ffa-server.accesswidener"))
}




