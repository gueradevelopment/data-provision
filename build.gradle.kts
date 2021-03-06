import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    extra.set("kotlinVersion", "1.3.21")
    dependencies {
        val kotlinVersion: String by rootProject.extra
        classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
    }
}

plugins {
    `java-library`
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.21"
    id("org.springframework.boot") version "2.1.3.RELEASE"
    id("org.jetbrains.kotlin.jvm") version "1.3.21"
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.3.21"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.3.21"
}

apply(plugin = "kotlin-jpa")
apply(plugin = "io.spring.dependency-management")

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    api("com.fasterxml.jackson.core:jackson-core:2.9.8")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
    api("khttp:khttp:0.1.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}