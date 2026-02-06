package org.stefanoprivitera.klock.domain.request

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.CustomerId

sealed interface CustomerRequest {
    @Serializable
    data class Create(
        val vatNumber: String,
        val taxCode: String,
        val companyName: String,
        val name: String,
        val email: String,
        val phone: String,
        val address: String
    ) : CustomerRequest

    @Serializable
    data class Update(
        val id: CustomerId,
        val vatNumber: String?,
        val taxCode: String?,
        val companyName: String?,
        val name: String?,
        val email: String?,
        val phone: String?,
        val address: String?
    ) : CustomerRequest

    @Serializable
    data class Filter(
        val companyName: String?,
        val name: String?,
        val email: String?
    ) : CustomerRequest
}
