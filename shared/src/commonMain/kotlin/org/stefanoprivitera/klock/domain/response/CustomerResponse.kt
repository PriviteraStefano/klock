package org.stefanoprivitera.klock.domain.response

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.Customer
import org.stefanoprivitera.klock.domain.CustomerId
import kotlin.uuid.ExperimentalUuidApi



@Serializable
@OptIn(ExperimentalUuidApi::class)
data class CustomerResponse(
    val id: CustomerId,
    val vatNumber: String,
    val taxCode: String,
    val companyName: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun from(customer: Customer): CustomerResponse = CustomerResponse(
            id = customer.id,
            vatNumber = customer.vatNumber,
            taxCode = customer.taxCode,
            companyName = customer.companyName,
            name = customer.name,
            email = customer.email,
            phone = customer.phone,
            address = customer.address,
            createdAt = customer.createdAt,
            updatedAt = customer.updatedAt
        )
    }
}
