package org.stefanoprivitera.klock.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Expense(
    val id: Uuid,
    val userId: Uuid,
    val date: LocalDate,
    val category: String,
    val amount: Double,
    val currency: String,
    val status: String, // "pending", "approved", "rejected"
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

sealed interface ExpenseRequest {
    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Create(
        val userId: Uuid,
        val date: LocalDate,
        val category: String,
        val amount: Double,
        val currency: String,
        val status: String = "pending"
    ) : ExpenseRequest

    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Update(
        val id: Uuid,
        val date: LocalDate?,
        val category: String?,
        val amount: Double?,
        val currency: String?,
        val status: String?
    ) : ExpenseRequest

    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Filter(
        val userId: Uuid?,
        val dateFrom: LocalDate?,
        val dateTo: LocalDate?,
        val category: String?,
        val status: String?
    ) : ExpenseRequest
}

