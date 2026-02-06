package org.stefanoprivitera.klock.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.stefanoprivitera.klock.domain.TimeEntryId
import org.stefanoprivitera.klock.domain.request.TimeEntryRequest
import org.stefanoprivitera.klock.domain.response.TimeEntryResponse
import org.stefanoprivitera.klock.routes.util.FilterBuilder.toTimeEntryFilter
import org.stefanoprivitera.klock.service.TimeEntryService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Route.timeEntries() {
    val timeEntryService by inject<TimeEntryService>()

    route("/time-entries") {
        get {
            val filterRequest = call.queryParameters.toTimeEntryFilter()
            val timeEntries = timeEntryService.findAll(filterRequest).map { TimeEntryResponse.from(it) }
            call.respond(timeEntries)
        }

        post {
            val createRequest = runCatching { call.receiveNullable<TimeEntryRequest.Create>() }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            val createResponse = runCatching { timeEntryService.create(createRequest) }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Created, createResponse)
        }

        route("/{id}") {
            get {
                val id = call.parameters["id"]?.let { TimeEntryId(Uuid.parse(it)) }
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val timeEntry = timeEntryService.findById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(TimeEntryResponse.from(timeEntry))
            }

            put {
                val id = call.parameters["id"]?.let { TimeEntryId(Uuid.parse(it)) }
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updateRequest = runCatching { call.receiveNullable<TimeEntryRequest.Update>() }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                if (id != updateRequest.id) {
                    return@put call.respond(HttpStatusCode.BadRequest, "ID mismatch")
                }
                val updateResponse = runCatching { timeEntryService.update(updateRequest) }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.InternalServerError)
                if (updateResponse == 0) {
                    return@put call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }

            delete {
                val id = call.parameters["id"]?.let { TimeEntryId(Uuid.parse(it)) }
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleteResponse = runCatching { timeEntryService.deleteById(id) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
