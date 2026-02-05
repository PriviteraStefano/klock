package org.stefanoprivitera.klock.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate
import org.koin.ktor.ext.inject
import org.stefanoprivitera.klock.domain.ExpenseId
import org.stefanoprivitera.klock.domain.ExpenseRequest
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.service.ExpenseService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Route.expenses() {
    val expenseService by inject<ExpenseService>()

    route("/expenses") {
        get {
            val userId = call.request.queryParameters["userId"]?.let { UserId(Uuid.parse(it)) }
            val dateFrom = call.request.queryParameters["dateFrom"]?.let { LocalDate.parse(it) }
            val dateTo = call.request.queryParameters["dateTo"]?.let { LocalDate.parse(it) }
            val category = call.request.queryParameters["category"]
            val status = call.request.queryParameters["status"]
            val filterRequest = ExpenseRequest.Filter(userId, dateFrom, dateTo, category, status)
            val expenses = expenseService.findAll(filterRequest)
            call.respond(expenses)
        }

        post {
            val createRequest = runCatching { call.receiveNullable<ExpenseRequest.Create>() }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            val createResponse = runCatching { expenseService.create(createRequest) }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Created, createResponse)
        }

        route("/{id}") {
            get {
                val id = call.parameters["id"]?.let { ExpenseId(Uuid.parse(it)) }
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val expense = expenseService.findById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(expense)
            }

            put {
                val id = call.parameters["id"]?.let { ExpenseId(Uuid.parse(it)) }
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updateRequest = runCatching { call.receiveNullable<ExpenseRequest.Update>() }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                if (id != updateRequest.id) {
                    return@put call.respond(HttpStatusCode.BadRequest, "ID mismatch")
                }
                val updateResponse = runCatching { expenseService.update(updateRequest) }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.InternalServerError)
                if (updateResponse == 0) {
                    return@put call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }

            delete {
                val id = call.parameters["id"]?.let { ExpenseId(Uuid.parse(it)) }
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleteResponse = runCatching { expenseService.deleteById(id) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
