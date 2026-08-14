plugins {
    kotlin("jvm") version "2.3.21"
}

group = "ru.let.hippopotamus"
version = project.findProperty("version")!!

repositories {
    mavenCentral()
    maven("https://snapshots.kord.dev")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("dev.kord:kord-core:0.18.1")
    implementation("tools.jackson.module:jackson-module-kotlin:3.1.5")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}