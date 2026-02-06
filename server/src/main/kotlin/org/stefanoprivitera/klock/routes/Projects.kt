package org.stefanoprivitera.klock.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.stefanoprivitera.klock.domain.*
import org.stefanoprivitera.klock.domain.request.ProjectRequest
import org.stefanoprivitera.klock.domain.response.ProjectResponse
import org.stefanoprivitera.klock.routes.util.FilterBuilder.toProjectFilter
import org.stefanoprivitera.klock.service.ProjectService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Route.projects() {
    val projectService by inject<ProjectService>()

    route("/projects") {
        get {
            val filterRequest = call.queryParameters.toProjectFilter()
            val projects = projectService.findAll(filterRequest).map { ProjectResponse.from(it) }
            call.respond(projects)
        }

        post {
            val createRequest = runCatching { call.receiveNullable<ProjectRequest.Create>() }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            val createResponse = runCatching { projectService.create(createRequest) }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Created, createResponse)
        }

        route("/{id}") {
            get {
                val id = call.parameters["id"]?.let { ProjectId(Uuid.parse(it)) }
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val project = projectService.findById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(ProjectResponse.from(project))
            }

            put {
                val id = call.parameters["id"]?.let { ProjectId(Uuid.parse(it)) }
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updateRequest = runCatching { call.receiveNullable<ProjectRequest.Update>() }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                if (id != updateRequest.id) {
                    return@put call.respond(HttpStatusCode.BadRequest, "ID mismatch")
                }
                val updateResponse = runCatching { projectService.update(updateRequest) }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.InternalServerError)
                if (updateResponse == 0) {
                    return@put call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }

            delete {
                val id = call.parameters["id"]?.let { ProjectId(Uuid.parse(it)) }
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleteResponse = runCatching { projectService.deleteById(id) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
