package org.stefanoprivitera.klock


import io.ktor.openapi.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import org.koin.core.annotation.KoinApplication
import org.koin.ktor.plugin.KoinApplicationStarted
import org.koin.ktor.plugin.KoinApplicationStopped
import org.stefanoprivitera.klock.configuration.jwtAuth
import org.stefanoprivitera.klock.extension.jsonExtension
import org.stefanoprivitera.klock.extension.koinExtension
import org.stefanoprivitera.klock.routes.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@KoinApplication
object KlockApp

fun Application.module() {
    jsonExtension()
    koinExtension()
    install(SSE)


    jwtAuth()
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        swaggerUI(path = "swagger") {
            OpenApiInfo(title = "Klock API", version = "0.0.1")
        }
        authentication()
        authenticate {
            users()
            contracts()
            customers()
            departments()
            expenses()
            projects()
            requests()
            timeEntries()
            timeEntryItems()
            workGroups()
            workGroupUsers()
            userDepartments()
        }
    }

    monitor.subscribe(KoinApplicationStarted) {
        log.info("Koin started")
    }
    monitor.subscribe(KoinApplicationStopped) {
        log.info("Koin stopped")
    }
}





