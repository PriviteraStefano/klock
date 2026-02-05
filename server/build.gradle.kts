plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.ksp)
    application
}

group = "org.stefanoprivitera.klock"
version = "1.0.0"
application {
    mainClass.set("org.stefanoprivitera.klock.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.client.apache)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation("io.ktor:ktor-server-request-validation:3.4.0")
    // Dependency Injection
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)

    // Database
    implementation(libs.postgresql)
    implementation(libs.h2)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.exposed.json)
    // AI
    implementation(libs.koog.ktor)

    // OpenAPI and Swagger
    implementation("io.ktor:ktor-server-swagger:3.4.0")
    implementation("io.ktor:ktor-server-routing-openapi:3.4.0")


    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}