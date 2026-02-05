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
import org.stefanoprivitera.klock.domain.TimeEntry
import org.stefanoprivitera.klock.domain.TimeEntryId
import org.stefanoprivitera.klock.domain.TimeEntryRequest
import org.stefanoprivitera.klock.persistance.TimeEntries
import org.stefanoprivitera.klock.repository.TimeEntryRepository
import org.stefanoprivitera.klock.repository.mapper.toTimeEntry
import org.stefanoprivitera.klock.repository.utils.andWhereIfNotNull
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi

@Single
@OptIn(ExperimentalUuidApi::class)
class TimeEntryRepositoryImpl : TimeEntryRepository {
    override fun create(entry: TimeEntryRequest.Create): TimeEntryId {
        return transaction {
            TimeEntries.insertAndGetId {
                it[userId] = entry.userId.value
                it[date] = entry.date
                it[type] = entry.type
                it[totalHours] = entry.totalHours.toBigDecimal()
                it[status] = entry.status
                it[metadata] = entry.metadata
                it[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.let { TimeEntryId(it.value) }
        }
    }

    override fun update(entry: TimeEntryRequest.Update): Int {
        return transaction {
            TimeEntries.update({ TimeEntries.id eq entry.id.value }) {
                entry.date?.let { d -> it[date] = d }
                entry.type?.let { t -> it[type] = t }
                entry.totalHours?.let { h -> it[totalHours] = h.toBigDecimal() }
                entry.status?.let { s -> it[status] = s }
                entry.metadata?.let { m -> it[metadata] = m }
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }
        }
    }

    override fun findById(id: TimeEntryId): TimeEntry? {
        return transaction {
            TimeEntries.selectAll()
                .where { TimeEntries.id eq id.value }
                .map(::toTimeEntry)
                .firstOrNull()
        }
    }

    override fun findAll(filter: TimeEntryRequest.Filter): List<TimeEntry> {
        return transaction {
            TimeEntries.selectAll()
                .andWhereIfNotNull(filter.userId) { TimeEntries.userId eq it.value }
                .andWhereIfNotNull(filter.dateFrom) { TimeEntries.date greater it }
                .andWhereIfNotNull(filter.dateTo) { TimeEntries.date less it }
                .andWhereIfNotNull(filter.type) { TimeEntries.type eq it }
                .andWhereIfNotNull(filter.status) { TimeEntries.status eq it }
                .map(::toTimeEntry)
        }
    }

    override fun deleteById(id: TimeEntryId): Int {
        return transaction {
            TimeEntries.deleteWhere { TimeEntries.id eq id.value }
        }
    }
}
