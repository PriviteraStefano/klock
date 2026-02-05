package org.stefanoprivitera.klock.repository.impl

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Expense
import org.stefanoprivitera.klock.domain.ExpenseId
import org.stefanoprivitera.klock.domain.ExpenseRequest
import org.stefanoprivitera.klock.persistance.Expenses
import org.stefanoprivitera.klock.repository.ExpenseRepository
import org.stefanoprivitera.klock.repository.mapper.toExpense
import org.stefanoprivitera.klock.repository.utils.andWhereIfNotNull
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi

@Single
@OptIn(ExperimentalUuidApi::class)
class ExpenseRepositoryImpl : ExpenseRepository {
    override fun create(expense: ExpenseRequest.Create): ExpenseId {
        return transaction {
            ExpenseId(Expenses.insertAndGetId {
                it[userId] = expense.userId.value
                it[date] = expense.date
                it[category] = expense.category
                it[amount] = expense.amount.toBigDecimal()
                it[currency] = expense.currency
                it[status] = expense.status
                it[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.value)
        }
    }

    override fun update(expense: ExpenseRequest.Update): Int {
        return transaction {
            Expenses.update({ Expenses.id eq expense.id.value }) {
                expense.date?.let { d -> it[date] = d }
                expense.category?.let { c -> it[category] = c }
                expense.amount?.let { a -> it[amount] = a.toBigDecimal() }
                expense.currency?.let { c -> it[currency] = c }
                expense.status?.let { s -> it[status] = s }
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }
        }
    }

    override fun findById(id: ExpenseId): Expense? {
        return transaction {
            Expenses.selectAll()
                .where { Expenses.id eq id.value }
                .map(::toExpense)
                .firstOrNull()
        }
    }

    override fun findAll(filter: ExpenseRequest.Filter): List<Expense> {
        return transaction {
            Expenses.selectAll()
                .andWhereIfNotNull(filter.userId) { Expenses.userId eq it.value }
                .andWhereIfNotNull(filter.dateFrom) { Expenses.date greater it }
                .andWhereIfNotNull(filter.dateTo) { Expenses.date less it }
                .andWhereIfNotNull(filter.category) { Expenses.category eq it }
                .andWhereIfNotNull(filter.status) { Expenses.status eq it }
                .map(::toExpense)
        }
    }

    override fun deleteById(id: ExpenseId): Int {
        return transaction {
            Expenses.deleteWhere { Expenses.id eq id.value }
        }
    }

}
