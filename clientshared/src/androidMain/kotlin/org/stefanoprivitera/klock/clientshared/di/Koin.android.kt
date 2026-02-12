@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package org.stefanoprivitera.klock.clientshared.di

import android.content.Context
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual class ContextWrapper(val context: Context)

@Module
actual class NativeModule actual constructor() {
    @Single
    actual fun providesContextWrapper(scope: Scope): ContextWrapper = ContextWrapper(scope.get())

    @Single
    actual fun httpClientEngine(): HttpClientEngine = OkHttp.create {}
}