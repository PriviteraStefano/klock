package org.stefanoprivitera.klock.domain

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JvmInline
@Serializable
value class ContractId @OptIn(ExperimentalUuidApi::class) constructor(val value: Uuid)

@OptIn(ExperimentalUuidApi::class)
data class Contract(
    val id: ContractId,
    val customerId: CustomerId,
    val billingDate: LocalDate,
    val amount: Long, //FIXME: add BigDecimal support
    val currency: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)