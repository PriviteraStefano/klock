package org.stefanoprivitera.klock.domain.response

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.*
import kotlin.uuid.ExperimentalUuidApi

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class ExpenseResponse(
    val id: ExpenseId,
    val userId: UserId,
    val date: LocalDate,
    val category: String,
    val amount: Double,
    val currency: String,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(expense: Expense): ExpenseResponse = ExpenseResponse(
            id = expense.id,
            userId = expense.userId,
            date = expense.date,
            category = expense.category,
            amount = expense.amount,
            currency = expense.currency,
            status = expense.status,
            createdAt = expense.createdAt,
            updatedAt = expense.updatedAt
        )
    }
}
