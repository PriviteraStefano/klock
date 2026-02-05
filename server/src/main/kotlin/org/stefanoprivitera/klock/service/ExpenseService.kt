package org.stefanoprivitera.klock.service

import org.stefanoprivitera.klock.domain.Expense
import org.stefanoprivitera.klock.domain.ExpenseId
import org.stefanoprivitera.klock.domain.ExpenseRequest

interface ExpenseService {
    fun create(expense: ExpenseRequest.Create): ExpenseId
    fun update(expense: ExpenseRequest.Update): Int
    fun findById(id: ExpenseId): Expense?
    fun findAll(filter: ExpenseRequest.Filter): List<Expense>
    fun deleteById(id: ExpenseId): Int
}
