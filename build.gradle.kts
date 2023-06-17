plugins {
    kotlin("multiplatform") version "1.8.22"
}

group = "com.rjspies"
version = "1.0-alpha.1"

repositories {
    mavenCentral()
}

kotlin {
    macosX64("native") {
        binaries {
            executable()
        }
    }

    sourceSets {
        val nativeMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:2.3.1")
                implementation("io.ktor:ktor-client-cio:2.3.1")
            }
        }
        val nativeTest by getting
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "8.0"
    distributionType = Wrapper.DistributionType.ALL
}
