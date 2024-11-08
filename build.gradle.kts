plugins {
    kotlin("jvm") version "2.0.20"
}

group = "io.spektacle"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler:2.0.21")
    implementation("org.jetbrains.kotlinx:kotlinx-cli-jvm:0.3.6")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}