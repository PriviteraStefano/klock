package org.stefanoprivitera.klock.service.impl

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Expense
import org.stefanoprivitera.klock.domain.ExpenseId
import org.stefanoprivitera.klock.domain.ExpenseRequest
import org.stefanoprivitera.klock.repository.ExpenseRepository
import org.stefanoprivitera.klock.service.ExpenseService

@Single
class ExpenseServiceImpl(
    private val expenseRepository: ExpenseRepository
) : ExpenseService {
    override fun create(expense: ExpenseRequest.Create): ExpenseId {
        return expenseRepository.create(expense)
    }

    override fun update(expense: ExpenseRequest.Update): Int {
        return expenseRepository.update(expense)
    }

    override fun findById(id: ExpenseId): Expense? {
        return expenseRepository.findById(id)
    }

    override fun findAll(filter: ExpenseRequest.Filter): List<Expense> {
        return expenseRepository.findAll(filter)
    }

    override fun deleteById(id: ExpenseId): Int {
        return expenseRepository.deleteById(id)
    }
}
