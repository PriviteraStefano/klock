package org.stefanoprivitera.klock.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.stefanoprivitera.klock.domain.*
import org.stefanoprivitera.klock.domain.response.TimeEntryItemResponse
import org.stefanoprivitera.klock.service.TimeEntryItemService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Route.timeEntryItems() {
    val timeEntryItemService by inject<TimeEntryItemService>()

    route("/time-entry-items") {
        get {
            val timeEntryId = call.request.queryParameters["timeEntryId"]?.let { TimeEntryId(Uuid.parse(it)) }
            val projectId = call.request.queryParameters["projectId"]?.let { ProjectId(Uuid.parse(it)) }
            val approved = call.request.queryParameters["approved"]?.toBoolean()
            val filterRequest = TimeEntryItemRequest.Filter(timeEntryId, projectId, approved)
            val timeEntryItems = timeEntryItemService.findAll(filterRequest).map { TimeEntryItemResponse.from(it) }
            call.respond(timeEntryItems)
        }

        post {
            val createRequest = runCatching { call.receiveNullable<TimeEntryItemRequest.Create>() }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            val createResponse = runCatching { timeEntryItemService.create(createRequest) }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Created, createResponse)
        }

        route("/{id}") {
            get {
                val id = call.parameters["id"]?.let { TimeEntryItemId(Uuid.parse(it)) }
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val timeEntryItem = timeEntryItemService.findById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(TimeEntryItemResponse.from(timeEntryItem))
            }

            put {
                val id = call.parameters["id"]?.let { TimeEntryItemId(Uuid.parse(it)) }
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updateRequest = runCatching { call.receiveNullable<TimeEntryItemRequest.Update>() }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                if (id != updateRequest.id) {
                    return@put call.respond(HttpStatusCode.BadRequest, "ID mismatch")
                }
                val updateResponse = runCatching { timeEntryItemService.update(updateRequest) }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.InternalServerError)
                if (updateResponse == 0) {
                    return@put call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }

            delete {
                val id = call.parameters["id"]?.let { TimeEntryItemId(Uuid.parse(it)) }
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleteResponse = runCatching { timeEntryItemService.deleteById(id) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
