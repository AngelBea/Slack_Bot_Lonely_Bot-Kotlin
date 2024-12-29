val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    id("io.ktor.plugin") version "2.3.7"
    kotlin("jvm") version "1.8.0"
}

group = "com.lonelybot"
version = "0.0.1"
application {
    mainClass.set("com.lonelybot.ApplicationKt")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.jayway.jsonpath:json-path:2.8.0")
}

tasks.register("stage"){
    dependsOn("clean", "build")
}
tasks.jar{
    manifest.attributes["Main_Class"] = "com/lonelybot/Application.kt"
    val dependencies = configurations.runtimeClasspath.get().map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}