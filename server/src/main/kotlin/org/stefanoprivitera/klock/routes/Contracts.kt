package org.stefanoprivitera.klock.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate
import org.koin.ktor.ext.inject
import org.stefanoprivitera.klock.domain.ContractId
import org.stefanoprivitera.klock.domain.ContractRequest
import org.stefanoprivitera.klock.domain.CustomerId
import org.stefanoprivitera.klock.service.ContractService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Route.contracts() {
    val contractService by inject<ContractService>()

    route("/contracts") {
        get {
            val customerId = call.request.queryParameters["customerId"]?.let { CustomerId(Uuid.parse(it)) }
            val billingDateFrom = call.request.queryParameters["billingDateFrom"]?.let { LocalDate.parse(it) }
            val billingDateTo = call.request.queryParameters["billingDateTo"]?.let { LocalDate.parse(it) }
            val status = call.request.queryParameters["status"]
            val filterRequest = ContractRequest.Filter(customerId, billingDateFrom, billingDateTo, status)
            val contracts = contractService.findAll(filterRequest)
            call.respond(contracts)
        }

        post {
            val createRequest = runCatching { call.receiveNullable<ContractRequest.Create>() }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            val createResponse = runCatching { contractService.create(createRequest) }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Created, createResponse)
        }

        route("/{id}") {
            get {
                val id = call.parameters["id"]?.let { ContractId(Uuid.parse(it)) }
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val contract = contractService.findById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(contract)
            }

            put {
                val id = call.parameters["id"]?.let { ContractId(Uuid.parse(it)) }
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updateRequest = runCatching { call.receiveNullable<ContractRequest.Update>() }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                if (id != updateRequest.id) {
                    return@put call.respond(HttpStatusCode.BadRequest, "ID mismatch")
                }
                val updateResponse = runCatching { contractService.update(updateRequest) }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.InternalServerError)
                if (updateResponse == 0) {
                    return@put call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }

            delete {
                val id = call.parameters["id"]?.let { ContractId(Uuid.parse(it)) }
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleteResponse = runCatching { contractService.deleteById(id) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
