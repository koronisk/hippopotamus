plugins {
    kotlin("jvm") version "2.3.21"
    id("com.gradleup.shadow") version "9.6.0"
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
    implementation("org.yaml:snakeyaml:2.6")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "ru.let.hippopotamus.MainKt"
    }
}