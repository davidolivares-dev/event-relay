plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    application
}

group = "com.eventrelay"
version = "0.1.0"

application {
    mainClass.set("com.eventrelay.ApplicationKt")
}

repositories {
    mavenCentral()
}

val ktorVersion = "2.3.7"

dependencies {
    // Ktor server — the HTTP framework
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")               // Netty = the engine that handles TCP connections
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion") // Automatic JSON conversion
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")        // Centralized error handling

    // JSON serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Testing
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.9.22")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

    // Utility
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
}

tasks.test {
    useJUnitPlatform()
}
