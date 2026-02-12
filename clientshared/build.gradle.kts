@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.serialization)
    alias(libs.plugins.koin.compiler)
}


kotlin {
    jvmToolchain(17)

    androidLibrary {
        namespace = "org.stefanoprivitera.klock.clientshared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "clientshared"
        }
    }

    jvm()

    js {
        browser()
    }

    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "clientshared.js"
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.ktor.client.core)
            implementation(libs.bundles.ktor.common)
            implementation(libs.kotlinx.coroutines)

            api(libs.koin.core)
            api(libs.koin.annotations)
            implementation(libs.koin.compose.multiplatform)
            implementation(libs.koin.test)


        }

        commonTest.dependencies {
            implementation(libs.koin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
            api(libs.ktor.client.core)
            api(libs.ktor.client.okhttp)
        }

        jvmMain.dependencies {
            api(libs.ktor.client.core)
            api(libs.ktor.client.cio)
            implementation(libs.kotlinx.coroutines.swing)
        }

        jvmTest.dependencies {
        }

        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
    }

    // KSP Common sourceSet
    sourceSets.named("commonMain").configure {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }

}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
}


//kotlin.sourceSets.all {
//    languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
//    languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
//}