plugins {
    kotlin("jvm")
    id("fabric-loom")
}

val silkVersion = "1.10.3"

dependencies {
    modImplementation("net.silkmc:silk-commands:$silkVersion")
    modImplementation("net.silkmc:silk-core:$silkVersion")
    modImplementation("net.silkmc:silk-network:$silkVersion")
}
