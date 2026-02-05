package org.stefanoprivitera.klock.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.stefanoprivitera.klock.domain.WorkGroupId
import org.stefanoprivitera.klock.domain.WorkGroupRequest
import org.stefanoprivitera.klock.domain.response.WorkGroupResponse
import org.stefanoprivitera.klock.service.WorkGroupService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Route.workGroups() {
    val workGroupService by inject<WorkGroupService>()

    route("/work-groups") {
        get {
            val name = call.request.queryParameters["name"]
            val description = call.request.queryParameters["description"]
            val users = call.request.queryParameters["users"]?.split(",")
            val filterRequest = WorkGroupRequest.Filter(name, description, users)
            val workGroups = workGroupService.findAll(filterRequest).map { WorkGroupResponse.from(it) }
            call.respond(workGroups)
        }

        post {
            val createRequest = runCatching { call.receiveNullable<WorkGroupRequest.Create>() }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            val createResponse = runCatching { workGroupService.create(createRequest) }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Created, createResponse)
        }

        route("/{id}") {
            get {
                val id = call.parameters["id"]?.let { WorkGroupId(Uuid.parse(it)) }
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val workGroup = workGroupService.findById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(WorkGroupResponse.from(workGroup))
            }

            put {
                val id = call.parameters["id"]?.let { WorkGroupId(Uuid.parse(it)) }
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updateRequest = runCatching { call.receiveNullable<WorkGroupRequest.Update>() }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                if (id != updateRequest.id) {
                    return@put call.respond(HttpStatusCode.BadRequest, "ID mismatch")
                }
                val updateResponse = runCatching { workGroupService.update(updateRequest) }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.InternalServerError)
                if (updateResponse == 0) {
                    return@put call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }

            delete {
                val id = call.parameters["id"]?.let { WorkGroupId(Uuid.parse(it)) }
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleteResponse = runCatching { workGroupService.deleteById(id) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
