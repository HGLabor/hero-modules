plugins {
    fabric
    kotlin
    `hglabor-publish`
    kotlin("plugin.serialization")
}

version = "$${property("mcVersion")}-1.0.0"
