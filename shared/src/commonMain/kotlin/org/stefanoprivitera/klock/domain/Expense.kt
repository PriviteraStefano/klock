package org.stefanoprivitera.klock.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JvmInline
@Serializable
value class ExpenseId @OptIn(ExperimentalUuidApi::class) constructor(val value: Uuid)

@OptIn(ExperimentalUuidApi::class)
data class Expense(
    val id: ExpenseId,
    val userId: UserId,
    val date: LocalDate,
    val category: String,
    val amount: Double,
    val currency: String,
    val status: String, // "pending", "approved", "rejected"
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

sealed interface ExpenseRequest {
    @Serializable
    data class Create(
        val userId: UserId,
        val date: LocalDate,
        val category: String,
        val amount: Double,
        val currency: String,
        val status: String = "pending"
    ) : ExpenseRequest

    @Serializable
    data class Update(
        val id: ExpenseId,
        val date: LocalDate?,
        val category: String?,
        val amount: Double?,
        val currency: String?,
        val status: String?
    ) : ExpenseRequest

    @Serializable
    data class Filter(
        val userId: UserId?,
        val dateFrom: LocalDate?,
        val dateTo: LocalDate?,
        val category: String?,
        val status: String?
    ) : ExpenseRequest
}

