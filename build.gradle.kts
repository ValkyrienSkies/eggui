plugins {
    kotlin("multiplatform") version "1.9.0"
    id("io.kotest.multiplatform") version "5.7.2"
}

val kotestVersion = "5.7.2"

group = "com.ewoudje"
version = "1.0-SNAPSHOT"



repositories {
    mavenCentral()
}

kotlin {
    targets {
        jvm {
            jvmToolchain(17)
            withJava()
        }
    }

    targets.all {
        compilations.all {
            kotlinOptions {
                verbose = true
            }
        }
    }
    
    sourceSets {
        val commonMain by getting
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")
                implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}