@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package org.stefanoprivitera.klock.clientshared.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope


actual class ContextWrapper

@Module
actual class NativeModule actual constructor() {
    @Single
    actual fun providesContextWrapper(scope: Scope): ContextWrapper = ContextWrapper()

    @Single
    actual fun httpClientEngine(): HttpClientEngine = Darwin.create()
}