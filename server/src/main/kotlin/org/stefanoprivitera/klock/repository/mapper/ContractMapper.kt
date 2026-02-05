package org.stefanoprivitera.klock.repository.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import org.stefanoprivitera.klock.domain.Contract
import org.stefanoprivitera.klock.domain.ContractId
import org.stefanoprivitera.klock.domain.CustomerId
import org.stefanoprivitera.klock.persistance.Contracts
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun toContract(r: ResultRow): Contract {
    return Contract(
        id = ContractId(r[Contracts.id].value),
        customerId = CustomerId(r[Contracts.customerId].value),
        billingDate = r[Contracts.billingDate],
        amount = r[Contracts.amount],
        currency = r[Contracts.currency],
        status = r[Contracts.status],
        createdAt = r[Contracts.createdAt].toString(),
        updatedAt = r[Contracts.updatedAt].toString()
    )
}