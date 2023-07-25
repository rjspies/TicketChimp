plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.io.gitlab.arturbosch.detekt)
    alias(libs.plugins.org.jmailen.kotlinter)
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
