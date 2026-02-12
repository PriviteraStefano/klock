@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package org.stefanoprivitera.klock.clientshared.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.core.annotation.*
import org.koin.core.scope.Scope
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes
import org.koin.plugin.module.dsl.startKoin

@KoinApplication
object KoinApp

fun initKoin(enableNetworkLogs: Boolean = false, appDeclaration: KoinAppDeclaration? = null) =
    KoinApp.startKoin<KoinApp> {
        includes(appDeclaration)
    }

// called by iOS etc
fun initKoin() = initKoin(enableNetworkLogs = false)

@Configuration
@Module(includes = [CommonModule::class])
class AppModule

@Module(includes = [NativeModule::class])
@ComponentScan("org.stefanoprivitera.klock")
class CommonModule {
    @Single
    fun json() = Json { isLenient = true; ignoreUnknownKeys = true }

    @Single
    fun httpClient(httpClientEngine: HttpClientEngine, json : Json) = createHttpClient(httpClientEngine, json, true)

    @Single
    fun dispatcher() = CoroutineScope(Dispatchers.Default + SupervisorJob() )
}

//@Module
//expect class ViewModelModule()

expect class ContextWrapper

@Module
expect class NativeModule() {

    @Single
    fun providesContextWrapper(scope : Scope) : ContextWrapper

    @Single
    fun httpClientEngine(): HttpClientEngine
}

fun createHttpClient(httpClientEngine: HttpClientEngine, json: Json, enableNetworkLogs: Boolean) = HttpClient(httpClientEngine) {
    install(ContentNegotiation) {
        json(json)
    }
    if (enableNetworkLogs) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }
}