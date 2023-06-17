plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.org.jlleitschuh.gradle.ktlint)
    alias(libs.plugins.io.gitlab.arturbosch.detekt)
}

group = libs.versions.group.get()
version = libs.versions.version.get()

repositories {
    mavenCentral()
}

kotlin {
    macosX64("native") {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val nativeMain by getting {
            dependencies {
                implementation(libs.bundles.implementation)
            }
        }
    }
}
