package org.stefanoprivitera.klock.repository.impl

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.TimeEntryItem
import org.stefanoprivitera.klock.domain.TimeEntryItemRequest
import org.stefanoprivitera.klock.persistance.TimeEntryItems
import org.stefanoprivitera.klock.repository.TimeEntryItemRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
@OptIn(ExperimentalUuidApi::class)
class TimeEntryItemRepositoryImpl : TimeEntryItemRepository {
    override fun create(item: TimeEntryItemRequest.Create): Uuid {
        return transaction {
            TimeEntryItems.insertAndGetId {
                it[timeEntryId] = item.timeEntryId
                it[projectId] = item.projectId
                it[hours] = item.hours.toBigDecimal()
                it[approved] = false
            }.value
        }
    }

    override fun update(item: TimeEntryItemRequest.Update): Int {
        return transaction {
            TimeEntryItems.update({ TimeEntryItems.id eq item.id }) {
                item.projectId?.let { p -> it[projectId] = p }
                item.hours?.let { h -> it[hours] = h.toBigDecimal() }
                item.approved?.let { a -> it[approved] = a }
            }
        }
    }

    override fun findById(id: Uuid): TimeEntryItem? {
        return transaction {
            TimeEntryItems.selectAll()
                .where { TimeEntryItems.id eq id }
                .map(::toTimeEntryItem)
                .firstOrNull()
        }
    }

    override fun findAll(filter: TimeEntryItemRequest.Filter): List<TimeEntryItem> {
        return transaction {
            var query = TimeEntryItems.selectAll()

            filter.timeEntryId?.let { query = query.where { TimeEntryItems.timeEntryId eq it } }
            filter.projectId?.let { query = query.where { TimeEntryItems.projectId eq it } }
            filter.approved?.let { query = query.where { TimeEntryItems.approved eq it } }

            query.map(::toTimeEntryItem)
        }
    }

    override fun deleteById(id: Uuid): Int {
        return transaction {
            TimeEntryItems.deleteWhere { TimeEntryItems.id eq id }
        }
    }

    override fun deleteByTimeEntryId(timeEntryId: Uuid): Int {
        return transaction {
            TimeEntryItems.deleteWhere { TimeEntryItems.timeEntryId eq timeEntryId }
        }
    }

    private fun toTimeEntryItem(r: ResultRow): TimeEntryItem {
        return TimeEntryItem(
            id = r[TimeEntryItems.id].value,
            timeEntryId = r[TimeEntryItems.timeEntryId].value,
            projectId = r[TimeEntryItems.projectId].value,
            hours = r[TimeEntryItems.hours].toDouble(),
            approved = r[TimeEntryItems.approved]
        )
    }
}
