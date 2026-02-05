package org.stefanoprivitera.klock

import io.ktor.openapi.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.annotation.KoinApplication
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin
import org.koin.ktor.plugin.KoinApplicationStarted
import org.koin.ktor.plugin.KoinApplicationStopped
import org.koin.logger.slf4jLogger
import org.stefanoprivitera.klock.configuration.jwtAuth
import org.stefanoprivitera.klock.routes.authentication
import org.stefanoprivitera.klock.routes.users

fun main(args: Array<String>): Unit = EngineMain.main(args)

@KoinApplication
object KlockApp

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        createEagerInstances()
        modules(KlockServerModule().module)
    }
    routing {
        swaggerUI(path = "swagger") {
            info = OpenApiInfo(title = "My API", version = "1.0.0")
        }
    }
    jwtAuth ()
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        authentication()
        authenticate {
            users()

        }
    }

    monitor.subscribe(KoinApplicationStarted) {
        log.info("Koin started")
    }
    monitor.subscribe(KoinApplicationStopped) {
        log.info("Koin stopped")
    }
}





