plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

val javaVersion = 17

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(javaVersion)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "$javaVersion"
        kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
    }
}

kotlin {
    jvmToolchain(javaVersion)

    sourceSets.all {
        languageSettings.optIn("kotlin.RequiresOptIn")
    }
}
