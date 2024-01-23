plugins {
    kotlin("multiplatform") version "1.9.0"
    id("io.kotest.multiplatform") version "5.7.2"
    id("maven-publish")
}

val kotestVersion = "5.7.2"

group = "com.ewoudje"
val gitRevision = "git rev-parse HEAD".execute()
version = "0.1.0+" + gitRevision.substring(0, 10)

repositories {
    mavenCentral()
    maven {
        name = "VS Maven"
        url = uri(project.findProperty("vs_maven_url") ?: "https://maven.valkyrienskies.org/")

        val vsMavenUsername = project.findProperty("vs_maven_username") as String?
        val vsMavenPassword = project.findProperty("vs_maven_password") as String?
        if (vsMavenPassword != null && vsMavenUsername != null) {
            credentials {
                username = vsMavenUsername
                password = vsMavenPassword
            }
        }
    }
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

    publishing {
        repositories {
            val vsMavenUsername = project.findProperty("vs_maven_username") as String?
            val vsMavenPassword = project.findProperty("vs_maven_password") as String?
            val vsMavenUrl = project.findProperty("vs_maven_url") as String?
            if (vsMavenUrl != null && vsMavenPassword != null && vsMavenUsername != null) {
                println("Publishing to VS Maven ($version)")
                maven {
                    url = uri(vsMavenUrl)
                    credentials {
                        username = vsMavenUsername
                        password = vsMavenPassword
                    }
                }
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

fun String.execute(envp: Array<String>? = null, dir: File = projectDir): String {
    val process = Runtime.getRuntime().exec(this, envp, dir)
    return process.inputStream.reader().readText()
}