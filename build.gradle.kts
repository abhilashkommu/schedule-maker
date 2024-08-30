plugins {
    kotlin("jvm") version "1.9.23"
}

group = "com.akommu.tools"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(18)
}

tasks.register<JavaExec>("run") {
    group = "application"
    description = "Run the main class"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.akommu.tools.MainKt")
}