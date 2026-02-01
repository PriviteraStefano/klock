package org.stefanoprivitera.klock.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
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
