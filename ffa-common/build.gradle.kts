plugins {
    fabric
    kotlin
    heroes
    kotlin("plugin.serialization")
}

version = "${BuildConstants.minecraftVersion}-1.0.1"

repositories {
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
    //Das hier ist hart ungeil eig sollte das ein buildskript sein aber er findet es dann nciht???
    //the mods should work with this!
    modImplementation("maven.modrinth:iris:1.7.3+1.21")
    modImplementation("maven.modrinth:sodium:mc1.21-0.5.11")
    modImplementation("maven.modrinth:nvidium:0.2.9-beta")
}
