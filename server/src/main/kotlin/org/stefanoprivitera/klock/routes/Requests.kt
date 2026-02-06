package org.stefanoprivitera.klock.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.stefanoprivitera.klock.domain.RequestId
import org.stefanoprivitera.klock.domain.request.RequestRequest
import org.stefanoprivitera.klock.domain.response.RequestResponse
import org.stefanoprivitera.klock.routes.util.FilterBuilder.toRequestFilter
import org.stefanoprivitera.klock.service.RequestService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Route.requests() {
    val requestService by inject<RequestService>()

    route("/requests") {
        get {
            val filterRequest = call.queryParameters.toRequestFilter()
            val requests = requestService.findAll(filterRequest).map { RequestResponse.from(it) }
            call.respond(requests)
        }

        post {
            val createRequest = runCatching { call.receiveNullable<RequestRequest.Create>() }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            val createResponse = runCatching { requestService.create(createRequest) }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Created, createResponse)
        }

        route("/{id}") {
            get {
                val id = call.parameters["id"]?.let { RequestId(Uuid.parse(it)) }
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val request = requestService.findById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(RequestResponse.from(request))
            }

            put {
                val id = call.parameters["id"]?.let { RequestId(Uuid.parse(it)) }
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updateRequest = runCatching { call.receiveNullable<RequestRequest.Update>() }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                if (id != updateRequest.id) {
                    return@put call.respond(HttpStatusCode.BadRequest, "ID mismatch")
                }
                val updateResponse = runCatching { requestService.update(updateRequest) }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.InternalServerError)
                if (updateResponse == 0) {
                    return@put call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }

            delete {
                val id = call.parameters["id"]?.let { RequestId(Uuid.parse(it)) }
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleteResponse = runCatching { requestService.deleteById(id) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
