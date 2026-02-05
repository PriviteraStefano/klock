package org.stefanoprivitera.klock.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.stefanoprivitera.klock.domain.DepartmentId
import org.stefanoprivitera.klock.domain.DepartmentRequest
import org.stefanoprivitera.klock.domain.response.DepartmentResponse
import org.stefanoprivitera.klock.service.DepartmentService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Route.departments() {
    val departmentService by inject<DepartmentService>()

    route("/departments") {
        get {
            val name = call.request.queryParameters["name"]
            val description = call.request.queryParameters["description"]
            val parentDepartmentId = call.request.queryParameters["parentDepartmentId"]?.let { DepartmentId(Uuid.parse(it)) }
            val filterRequest = DepartmentRequest.Filter(name, description, parentDepartmentId)
            val departments = departmentService.findAll(filterRequest).map { DepartmentResponse.from(it) }
            call.respond(departments)
        }

        post {
            val createRequest = runCatching { call.receiveNullable<DepartmentRequest.Create>() }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            val createResponse = runCatching { departmentService.create(createRequest) }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Created, createResponse)
        }

        route("/{id}") {
            get {
                val id = call.parameters["id"]?.let { DepartmentId(Uuid.parse(it)) }
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val department = departmentService.findById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(DepartmentResponse.from(department))
            }

            put {
                val id = call.parameters["id"]?.let { DepartmentId(Uuid.parse(it)) }
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updateRequest = runCatching { call.receiveNullable<DepartmentRequest.Update>() }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                if (id != updateRequest.id) {
                    return@put call.respond(HttpStatusCode.BadRequest, "ID mismatch")
                }
                val updateResponse = runCatching { departmentService.update(updateRequest) }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.InternalServerError)
                if (updateResponse == 0) {
                    return@put call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }

            delete {
                val id = call.parameters["id"]?.let { DepartmentId(Uuid.parse(it)) }
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleteResponse = runCatching { departmentService.deleteById(id) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
