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
import org.stefanoprivitera.klock.domain.TimeEntry
import org.stefanoprivitera.klock.domain.TimeEntryRequest
import org.stefanoprivitera.klock.persistance.TimeEntries
import org.stefanoprivitera.klock.repository.TimeEntryRepository
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
@OptIn(ExperimentalUuidApi::class)
class TimeEntryRepositoryImpl : TimeEntryRepository {
    override fun create(entry: TimeEntryRequest.Create): String {
        return transaction {
            TimeEntries.insertAndGetId {
                it[userId] = entry.userId
                it[date] = entry.date
                it[type] = entry.type
                it[totalHours] = entry.totalHours.toBigDecimal()
                it[status] = entry.status
                it[metadata] = entry.metadata
                it[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.value.toString()
        }
    }

    override fun update(entry: TimeEntryRequest.Update): Int {
        return transaction {
            TimeEntries.update({ TimeEntries.id eq entry.id }) {
                entry.date?.let { d -> it[date] = d }
                entry.type?.let { t -> it[type] = t }
                entry.totalHours?.let { h -> it[totalHours] = h.toBigDecimal() }
                entry.status?.let { s -> it[status] = s }
                entry.metadata?.let { m -> it[metadata] = m }
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }
        }
    }

    override fun findById(id: String): TimeEntry? {
        return transaction {
            val uuid = Uuid.parse(id)
            TimeEntries.selectAll()
                .where { TimeEntries.id eq uuid }
                .map(::toTimeEntry)
                .firstOrNull()
        }
    }

    override fun findAll(filter: TimeEntryRequest.Filter): List<TimeEntry> {
        return transaction {
            var query = TimeEntries.selectAll()

            filter.userId?.let {
                query = query.where { TimeEntries.userId eq it }
            }
            filter.dateFrom?.let {
                query = query.where { TimeEntries.date greater it }
            }
            filter.dateTo?.let {
                query = query.where { TimeEntries.date less it }
            }
            filter.type?.let {
                query = query.where { TimeEntries.type eq it }
            }
            filter.status?.let {
                query = query.where { TimeEntries.status eq it }
            }

            query.map(::toTimeEntry)
        }
    }

    override fun deleteById(id: String): Int {
        return transaction {
            val uuid = Uuid.parse(id)
            TimeEntries.deleteWhere { TimeEntries.id eq uuid }
        }
    }

    private fun toTimeEntry(r: ResultRow): TimeEntry {
        return TimeEntry(
            id = r[TimeEntries.id].value,
            userId = r[TimeEntries.userId].value,
            date = r[TimeEntries.date],
            type = r[TimeEntries.type],
            totalHours = r[TimeEntries.totalHours].toDouble(),
            status = r[TimeEntries.status],
            metadata = r[TimeEntries.metadata],
            createdAt = r[TimeEntries.createdAt],
            updatedAt = r[TimeEntries.updatedAt]
        )
    }
}
