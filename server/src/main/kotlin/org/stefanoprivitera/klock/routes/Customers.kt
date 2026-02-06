package org.stefanoprivitera.klock.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.stefanoprivitera.klock.domain.CustomerId
import org.stefanoprivitera.klock.domain.request.CustomerRequest
import org.stefanoprivitera.klock.domain.response.CustomerResponse
import org.stefanoprivitera.klock.service.CustomerService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Route.customers() {
    val customerService by inject<CustomerService>()

    route("/customers") {
        get {
            val companyName = call.request.queryParameters["companyName"]
            val name = call.request.queryParameters["name"]
            val email = call.request.queryParameters["email"]
            val filterRequest = CustomerRequest.Filter(companyName, name, email)
            val customers = customerService.findAll(filterRequest).map { CustomerResponse.from(it) }
            call.respond(customers)
        }

        post {
            val createRequest = runCatching { call.receiveNullable<CustomerRequest.Create>() }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            val createResponse = runCatching { customerService.create(createRequest) }.getOrNull()
                ?: return@post call.respond(HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Created, createResponse)
        }

        route("/{id}") {
            get {
                val id = call.parameters["id"]?.let { CustomerId(Uuid.parse(it)) }
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val customer = customerService.findById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(CustomerResponse.from(customer))
            }

            put {
                val id = call.parameters["id"]?.let { CustomerId(Uuid.parse(it)) }
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updateRequest = runCatching { call.receiveNullable<CustomerRequest.Update>() }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                if (id != updateRequest.id) {
                    return@put call.respond(HttpStatusCode.BadRequest, "ID mismatch")
                }
                val updateResponse = runCatching { customerService.update(updateRequest) }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.InternalServerError)
                if (updateResponse == 0) {
                    return@put call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }

            delete {
                val id = call.parameters["id"]?.let { CustomerId(Uuid.parse(it)) }
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleteResponse = runCatching { customerService.deleteById(id) }.getOrNull()
                    ?: return@delete call.respond(HttpStatusCode.InternalServerError)
                if (deleteResponse == 0) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
