package org.stefanoprivitera.klock

import io.ktor.openapi.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.mcp
import io.modelcontextprotocol.kotlin.sdk.types.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
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

//    koogExtension()

    val mcpServer = Server(
        serverInfo = Implementation(
            name = "example-server",
            version = "1.0.0"
        ),
        options = ServerOptions(
            capabilities = ServerCapabilities(
                tools = ServerCapabilities.Tools(listChanged = true),
            ),
        )
    )
    mcpServer.addTool(
        name = "example-tool",
        description = "An example tool",
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                put("input", buildJsonObject { put("type", "string") })
            }
        )
    ) { request ->
        CallToolResult(content = listOf(TextContent("Hello, world!")))
    }

    jwtAuth()
    routing {
        /*mcp {
            mcpServer
        }*/

        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        swaggerUI(path = "swagger") {
            info = OpenApiInfo(title = "My API", version = "1.0.0")
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





