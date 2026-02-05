package org.stefanoprivitera.klock.domain.response

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.*
import kotlin.uuid.ExperimentalUuidApi

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class ContractResponse(
    val id: ContractId,
    val customerId: CustomerId,
    val billingDate: LocalDate,
    val amount: Long,
    val currency: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun from(contract: Contract): ContractResponse = ContractResponse(
            id = contract.id,
            customerId = contract.customerId,
            billingDate = contract.billingDate,
            amount = contract.amount,
            currency = contract.currency,
            status = contract.status,
            createdAt = contract.createdAt,
            updatedAt = contract.updatedAt
        )
    }
}
