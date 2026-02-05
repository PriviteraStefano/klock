package org.stefanoprivitera.klock.repository.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import org.stefanoprivitera.klock.domain.Expense
import org.stefanoprivitera.klock.domain.ExpenseId
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.persistance.Expenses
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun toExpense(r: ResultRow): Expense {
    return Expense(
        id = ExpenseId(r[Expenses.id].value),
        userId = UserId(r[Expenses.userId].value),
        date = r[Expenses.date],
        category = r[Expenses.category],
        amount = r[Expenses.amount].toDouble(),
        currency = r[Expenses.currency],
        status = r[Expenses.status],
        createdAt = r[Expenses.createdAt],
        updatedAt = r[Expenses.updatedAt]
    )
}