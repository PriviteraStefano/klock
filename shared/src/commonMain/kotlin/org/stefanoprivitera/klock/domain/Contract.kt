package org.stefanoprivitera.klock.domain

import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Contract(
    val id: Uuid,
    val customerId: Uuid,
    val billingDate: LocalDate,
    val amount: Long, //FIXME: add BigDecimal support
    val currency: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

sealed interface ContractRequest {
    @OptIn(ExperimentalUuidApi::class)
    data class Create(
        val customerId: Uuid,
        val billingDate: LocalDate,
        val amount: Long, //FIXME: add BigDecimal support
        val currency: String,
        val status: String
    ) : ContractRequest

    @OptIn(ExperimentalUuidApi::class)
    data class Update(
        val id: Uuid,
        val customerId: Uuid?,
        val billingDate: LocalDate?,
        val amount: Long?, //FIXME: add BigDecimal support
        val currency: String?,
        val status: String?
    ) : ContractRequest

    @OptIn(ExperimentalUuidApi::class)
    data class Filter(
        val customerId: Uuid?,
        val billingDateFrom: LocalDate?,
        val billingDateTo: LocalDate?,
        val status: String?
    ) : ContractRequest
}