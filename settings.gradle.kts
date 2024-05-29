rootProject.name = "hero-modules"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
    }
}

include("ffa-common")
include("ffa-client")
include("ffa-server")

