package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.Expense
import org.stefanoprivitera.klock.domain.ExpenseRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface ExpenseRepository {
    fun create(expense: ExpenseRequest.Create): Uuid
    fun update(expense: ExpenseRequest.Update): Int
    fun findById(id: Uuid): Expense?
    fun findAll(filter: ExpenseRequest.Filter): List<Expense>
    fun deleteById(id: Uuid): Int
}
