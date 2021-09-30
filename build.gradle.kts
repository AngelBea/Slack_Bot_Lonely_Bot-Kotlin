val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.31"
}

group = "com.lonelybot"
version = "0.0.1"
application {
    mainClass.set("com.lonelybot.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    implementation("com.slack.api:bolt:1.1.+")
    implementation("com.slack.api:bolt-servlet:1.1.+")
    implementation("com.slack.api:bolt-jetty:1.1.+")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
}