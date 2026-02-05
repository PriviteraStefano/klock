package org.stefanoprivitera.klock.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.stefanoprivitera.klock.domain.*
import org.stefanoprivitera.klock.domain.response.UserDepartmentResponse
import org.stefanoprivitera.klock.service.UserDepartmentService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Route.userDepartments() {
    val userDepartmentService by inject<UserDepartmentService>()

    route("/user-departments") {
        get {
            val userId = call.request.queryParameters["userId"]?.let { UserId(Uuid.parse(it)) }
            val departmentId = call.request.queryParameters["departmentId"]?.let { DepartmentId(Uuid.parse(it)) }
            val filterRequest = UserDepartmentRequest.Filter(userId, departmentId)
            val userDepartments = userDepartmentService.findAll(filterRequest).map { UserDepartmentResponse.from(it) }
            call.respond(userDepartments)
        }

        post {
            val createRequest = runCatching { call.receiveNullable<UserDepartmentRequest.Create>() }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            val createResponse = runCatching { userDepartmentService.create(createRequest) }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Created, createResponse)
        }

        delete {
            val userId = call.request.queryParameters["userId"]?.let { UserId(Uuid.parse(it)) }
            val departmentId = call.request.queryParameters["departmentId"]?.let { DepartmentId(Uuid.parse(it)) }

            if (userId != null && departmentId != null) {
                val deleteResponse = runCatching { userDepartmentService.deleteByUserAndDepartment(userId, departmentId) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Both userId and departmentId are required")
            }
        }

        route("/{id}") {
            delete {
                val id = call.parameters["id"]?.let { UserDepartmentId(Uuid.parse(it)) }
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleteResponse = runCatching { userDepartmentService.deleteById(id) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
