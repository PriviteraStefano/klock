package org.stefanoprivitera.klock.repository.impl

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.ResultRow
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
import org.stefanoprivitera.klock.domain.ExpenseRequest
import org.stefanoprivitera.klock.persistance.Expenses
import org.stefanoprivitera.klock.repository.ExpenseRepository
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
@OptIn(ExperimentalUuidApi::class)
class ExpenseRepositoryImpl : ExpenseRepository {
    override fun create(expense: ExpenseRequest.Create): Uuid {
        return transaction {
            Expenses.insertAndGetId {
                it[userId] = expense.userId
                it[date] = expense.date
                it[category] = expense.category
                it[amount] = expense.amount.toBigDecimal()
                it[currency] = expense.currency
                it[status] = expense.status
                it[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.value
        }
    }

    override fun update(expense: ExpenseRequest.Update): Int {
        return transaction {
            Expenses.update({ Expenses.id eq expense.id }) {
                expense.date?.let { d -> it[date] = d }
                expense.category?.let { c -> it[category] = c }
                expense.amount?.let { a -> it[amount] = a.toBigDecimal() }
                expense.currency?.let { c -> it[currency] = c }
                expense.status?.let { s -> it[status] = s }
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }
        }
    }

    override fun findById(id: Uuid): Expense? {
        return transaction {
            Expenses.selectAll()
                .where { Expenses.id eq id }
                .map(::toExpense)
                .firstOrNull()
        }
    }

    override fun findAll(filter: ExpenseRequest.Filter): List<Expense> {
        return transaction {
            var query = Expenses.selectAll()

            filter.userId?.let {
                query = query.where { Expenses.userId eq it }
            }
            filter.dateFrom?.let {
                query = query.where { Expenses.date greater it }
            }
            filter.dateTo?.let {
                query = query.where { Expenses.date less it }
            }
            filter.category?.let {
                query = query.where { Expenses.category eq it }
            }
            filter.status?.let {
                query = query.where { Expenses.status eq it }
            }

            query.map(::toExpense)
        }
    }

    override fun deleteById(id: Uuid): Int {
        return transaction {
            Expenses.deleteWhere { Expenses.id eq id }
        }
    }

    private fun toExpense(r: ResultRow): Expense {
        return Expense(
            id = r[Expenses.id].value,
            userId = r[Expenses.userId].value,
            date = r[Expenses.date],
            category = r[Expenses.category],
            amount = r[Expenses.amount].toDouble(),
            currency = r[Expenses.currency],
            status = r[Expenses.status],
            createdAt = r[Expenses.createdAt],
            updatedAt = r[Expenses.updatedAt]
        )
    }
}
