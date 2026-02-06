package org.stefanoprivitera.klock.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.stefanoprivitera.klock.domain.ExpenseId
import org.stefanoprivitera.klock.domain.request.ExpenseRequest
import org.stefanoprivitera.klock.domain.response.ExpenseResponse
import org.stefanoprivitera.klock.routes.util.FilterBuilder.toExpenseFilter
import org.stefanoprivitera.klock.service.ExpenseService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Route.expenses() {
    val expenseService by inject<ExpenseService>()

    route("/expenses") {
        get {
            val filterRequest = call.queryParameters.toExpenseFilter()
            val expenses = expenseService.findAll(filterRequest).map { ExpenseResponse.from(it) }
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
                call.respond(ExpenseResponse.from(expense))
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
