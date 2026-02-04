package org.stefanoprivitera.klock.domain

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Customer(
    val id: Uuid,
    val vatNumber: String,
    val taxCode: String,
    val companyName: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val createdAt: String,
    val updatedAt: String
)

sealed interface CustomerRequest {
    @OptIn(ExperimentalUuidApi::class)
    data class Create(
        val vatNumber: String,
        val taxCode: String,
        val companyName: String,
        val name: String,
        val email: String,
        val phone: String,
        val address: String
    ) : CustomerRequest

    @OptIn(ExperimentalUuidApi::class)
    data class Update(
        val id: Uuid,
        val vatNumber: String?,
        val taxCode: String?,
        val companyName: String?,
        val name: String?,
        val email: String?,
        val phone: String?,
        val address: String?
    ) : CustomerRequest

    @OptIn(ExperimentalUuidApi::class)
    data class Filter(
        val companyName: String?,
        val name: String?,
        val email: String?
    ) : CustomerRequest
}
