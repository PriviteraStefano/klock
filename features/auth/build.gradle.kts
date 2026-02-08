import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.serialization)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    androidLibrary {
        namespace = "org.stefanoprivitera.klock.features.authentication"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "AuthFeature"
            isStatic = true
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
            implementation(libs.kotlinx.coroutines)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.kotlinx.datetime)

            implementation(libs.bundles.ktor.common)
            api(libs.serialization.kotlinx.json)

            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.koin.test)
            implementation(libs.koin.compose.multiplatform)


            implementation(projects.shared)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}