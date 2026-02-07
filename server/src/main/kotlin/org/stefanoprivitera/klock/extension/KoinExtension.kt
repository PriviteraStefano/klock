package org.stefanoprivitera.klock.extension

import io.ktor.server.application.*
import org.koin.dsl.koinConfiguration
import org.koin.ksp.generated.configurationModules
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.stefanoprivitera.klock.KlockApp

fun Application.koinExtension() {
    install(Koin) {
        slf4jLogger()
        createEagerInstances()
        koinConfiguration { KlockApp.configurationModules }
    }
}