package org.stefanoprivitera.klock.domain.request

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.ContractId
import org.stefanoprivitera.klock.domain.CustomerId

sealed interface ContractRequest {
    @Serializable
    data class Create(
        val customerId: CustomerId,
        val billingDate: LocalDate,
        val amount: Long, //FIXME: add BigDecimal support
        val currency: String,
        val status: String
    ) : ContractRequest

    @Serializable
    data class Update(
        val id: ContractId,
        val customerId: CustomerId?,
        val billingDate: LocalDate?,
        val amount: Long?, //FIXME: add BigDecimal support
        val currency: String?,
        val status: String?
    ) : ContractRequest

    @Serializable
    data class Filter(
        val customerId: CustomerId?,
        val billingDateFrom: LocalDate?,
        val billingDateTo: LocalDate?,
        val status: String?
    ) : ContractRequest
}
