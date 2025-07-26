import org.gradle.kotlin.dsl.minecraft

plugins {
    id("fabric-loom") version "1.11-SNAPSHOT"
    id("maven-publish")
    kotlin("jvm") version "2.2.0"
}

group = "me.unidok"
version = property("mod_version")!!


repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")
}

kotlin {
    jvmToolchain(21)
}

tasks.processResources {
    filesMatching("fabric.mod.json") {
        expand(getProperties())
    }
}