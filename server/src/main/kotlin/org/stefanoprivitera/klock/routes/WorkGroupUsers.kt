package org.stefanoprivitera.klock.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.stefanoprivitera.klock.domain.*
import org.stefanoprivitera.klock.service.WorkGroupUserService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Route.workGroupUsers() {
    val workGroupUserService by inject<WorkGroupUserService>()

    route("/work-group-users") {
        get {
            val workGroupId = call.request.queryParameters["workGroupId"]?.let { WorkGroupId(Uuid.parse(it)) }
            val userId = call.request.queryParameters["userId"]?.let { UserId(Uuid.parse(it)) }
            val filterRequest = WorkGroupUserRequest.Filter(workGroupId, userId)
            val workGroupUsers = workGroupUserService.findAll(filterRequest)
            call.respond(workGroupUsers)
        }

        post {
            val createRequest = runCatching { call.receiveNullable<WorkGroupUserRequest.Create>() }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            val createResponse = runCatching { workGroupUserService.create(createRequest) }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Created, createResponse)
        }

        delete {
            val userId = call.request.queryParameters["userId"]?.let { UserId(Uuid.parse(it)) }
            val workGroupId = call.request.queryParameters["workGroupId"]?.let { WorkGroupId(Uuid.parse(it)) }

            if (userId != null && workGroupId != null) {
                val deleteResponse = runCatching { workGroupUserService.deleteWorkGroupUser(userId, workGroupId) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Both userId and workGroupId are required")
            }
        }

        route("/{id}") {
            delete {
                val id = call.parameters["id"]?.let { WorkGroupUserId(Uuid.parse(it)) }
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleteResponse = runCatching { workGroupUserService.deleteById(id) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
