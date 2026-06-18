plugins {
    id("org.jetbrains.kotlin.multiplatform") version "2.1.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
    id("maven-publish")
}

group = "org.oar.lib"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        browser {
            // No executable binaries here (this is a library)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
            }
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/oarsc/kotlin-js-blocks")

            credentials {
                username = System.getenv("GITHUB_USER")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
