rootProject.name = "hero-module"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
    }
}

include("ffa-common")
include("ffa-client")
include("ffa-server")

