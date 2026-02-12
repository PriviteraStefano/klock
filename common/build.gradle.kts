import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.serialization)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    androidLibrary {
        namespace = "org.stefanoprivitera.klock.common"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }
    

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "common"
            isStatic = true
            binaryOption("bundleId", "org.stefanoprivitera.klock.common")
            // Export all public types from dependencies
            export(projects.shared)
            export(projects.clientshared)
            export(projects.features.auth)
        }
    }
    
    jvm()
    
    js {
        browser()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.serialization.kotlinx.json)
            implementation(libs.kotlinx.datetime)
            api(projects.shared)
            api(projects.clientshared)
            api(projects.features.auth)

            api(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.koin.test)
            implementation(libs.koin.compose.multiplatform)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
}