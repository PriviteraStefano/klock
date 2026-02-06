package org.stefanoprivitera.klock.domain.request

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.ExpenseId
import org.stefanoprivitera.klock.domain.UserId

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
