package org.stefanoprivitera.klock.repository.impl

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.*
import org.stefanoprivitera.klock.domain.request.TimeEntryItemRequest
import org.stefanoprivitera.klock.persistance.TimeEntryItems
import org.stefanoprivitera.klock.repository.TimeEntryItemRepository
import org.stefanoprivitera.klock.repository.mapper.toTimeEntryItem
import org.stefanoprivitera.klock.repository.utils.andWhereIfNotNull
import kotlin.uuid.ExperimentalUuidApi

@Single
@OptIn(ExperimentalUuidApi::class)
class TimeEntryItemRepositoryImpl : TimeEntryItemRepository {
    override fun create(item: TimeEntryItemRequest.Create): TimeEntryItemId {
        return transaction {
            TimeEntryItemId(TimeEntryItems.insertAndGetId {
                it[timeEntryId] = item.timeEntryId.value
                it[projectId] = item.projectId.value
                it[hours] = item.hours.toBigDecimal()
                it[approved] = false
            }.value)
        }
    }

    override fun update(item: TimeEntryItemRequest.Update): Int {
        return transaction {
            TimeEntryItems.update({ TimeEntryItems.id eq item.id.value }) {
                item.projectId?.let { p -> it[projectId] = p.value }
                item.hours?.let { h -> it[hours] = h.toBigDecimal() }
                item.approved?.let { a -> it[approved] = a }
            }
        }
    }

    override fun findById(id: TimeEntryItemId): TimeEntryItem? {
        return transaction {
            TimeEntryItems.selectAll()
                .where { TimeEntryItems.id eq id.value }
                .map(::toTimeEntryItem)
                .firstOrNull()
        }
    }

    override fun findAll(filter: TimeEntryItemRequest.Filter): List<TimeEntryItem> {
        return transaction {
            TimeEntryItems.selectAll()
                .andWhereIfNotNull(filter.timeEntryId) { TimeEntryItems.timeEntryId eq it.value }
                .andWhereIfNotNull(filter.projectId) { TimeEntryItems.projectId eq it.value }
                .andWhereIfNotNull(filter.approved) { TimeEntryItems.approved eq it }
                .map(::toTimeEntryItem)
        }
    }

    override fun deleteById(id: TimeEntryItemId): Int {
        return transaction {
            TimeEntryItems.deleteWhere { TimeEntryItems.id eq id.value }
        }
    }

    override fun deleteByTimeEntryId(timeEntryId: TimeEntryId): Int {
        return transaction {
            TimeEntryItems.deleteWhere { TimeEntryItems.timeEntryId eq timeEntryId.value }
        }
    }
}
