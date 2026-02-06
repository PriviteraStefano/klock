package org.stefanoprivitera.klock.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.domain.request.UserRequest
import org.stefanoprivitera.klock.domain.response.UserResponse
import org.stefanoprivitera.klock.routes.util.FilterBuilder.toUserFilter
import org.stefanoprivitera.klock.service.UserService
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun Route.users() {
    val userService by inject<UserService>()

    route("/users") {
        get {
            val filterRequest = call.request.queryParameters.toUserFilter()
            val users = userService.findAll(filterRequest).map { UserResponse.from(it) }
            call.respond(users)
        }

        post {
            val createRequest = runCatching { call.receiveNullable<UserRequest.Create>() }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            val createResponse = runCatching { userService.create(createRequest) }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.InternalServerError)
            call.respond(createResponse)
        }
        route("/{id}") {
            get {
                val id = call.parameters["id"]?.let { UserId(it) }
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val user = userService.findById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(UserResponse.from(user))
            }
            put {
                val id = call.parameters["id"]?.let { UserId(it) }
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updateRequest = runCatching { call.receiveNullable<UserRequest.Update>() }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                if (id != updateRequest.id) {
                    return@put call.respond(HttpStatusCode.BadRequest)
                }
                val updateResponse = runCatching { userService.update(updateRequest) }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.InternalServerError)
                if (updateResponse == 0) {
                    return@put call.respond(HttpStatusCode.NotFound)
                }
            }
            delete {
                val id = call.parameters["id"]?.let { UserId(it) }
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleteResponse = runCatching { userService.deleteById(id) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}