import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm") version "1.7.21"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    application
}

group = "com.misterderpie"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.22")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.fasterxml.jackson.core:jackson-core:2.14.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")

    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.1")
    testImplementation("io.mockk:mockk:1.13.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

configure<KtlintExtension> {
    filter {
        exclude { it.file.path.contains("CommonParameters.kt") }
    }
}
