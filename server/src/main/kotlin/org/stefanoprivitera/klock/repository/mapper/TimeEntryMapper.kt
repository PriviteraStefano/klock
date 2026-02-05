package org.stefanoprivitera.klock.repository.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import org.stefanoprivitera.klock.domain.TimeEntry
import org.stefanoprivitera.klock.domain.TimeEntryId
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.persistance.TimeEntries
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun toTimeEntry(r: ResultRow): TimeEntry {
    return TimeEntry(
        id = TimeEntryId(r[TimeEntries.id].value),
        userId = UserId(r[TimeEntries.userId].value),
        date = r[TimeEntries.date],
        type = r[TimeEntries.type],
        totalHours = r[TimeEntries.totalHours].toDouble(),
        status = r[TimeEntries.status],
        metadata = r[TimeEntries.metadata],
        createdAt = r[TimeEntries.createdAt],
        updatedAt = r[TimeEntries.updatedAt]
    )
}