package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.Expense
import org.stefanoprivitera.klock.domain.ExpenseId
import org.stefanoprivitera.klock.domain.ExpenseRequest

interface ExpenseRepository {
    fun create(expense: ExpenseRequest.Create): ExpenseId
    fun update(expense: ExpenseRequest.Update): Int
    fun findById(id: ExpenseId): Expense?
    fun findAll(filter: ExpenseRequest.Filter): List<Expense>
    fun deleteById(id: ExpenseId): Int
}
