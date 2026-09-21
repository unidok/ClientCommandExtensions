import org.gradle.kotlin.dsl.minecraft

plugins {
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT"
    id("maven-publish")
    kotlin("jvm") version "2.3.0"
}

group = "me.unidok"
version = property("mod_version")!!


repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
    implementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")
}

kotlin {
    jvmToolchain(25)
}

tasks.processResources {
    filesMatching("fabric.mod.json") {
        expand(getProperties())
    }
}