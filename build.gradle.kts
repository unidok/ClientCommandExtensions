plugins {
    id("fabric-loom") version "1.7-SNAPSHOT"
    id("maven-publish")
    kotlin("jvm") version "2.0.21"
}

group = "me.unidok"
version = "1.1"


repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:1.21")
    mappings("net.fabricmc:yarn:1.21+build.3:v2")
    modImplementation("net.fabricmc:fabric-loader:0.15.0")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.107.0+1.21.1")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.12.3+kotlin.2.0.21")
}

kotlin {
    jvmToolchain(21)
}