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

