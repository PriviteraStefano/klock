package org.stefanoprivitera.klock.domain.response

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.*
import kotlin.uuid.ExperimentalUuidApi

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class TimeEntryResponse(
    val id: TimeEntryId,
    val userId: UserId,
    val date: LocalDate,
    val type: String,
    val totalHours: Double,
    val status: String,
    val metadata: TimeEntryMetadata,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(timeEntry: TimeEntry): TimeEntryResponse = TimeEntryResponse(
            id = timeEntry.id,
            userId = timeEntry.userId,
            date = timeEntry.date,
            type = timeEntry.type,
            totalHours = timeEntry.totalHours,
            status = timeEntry.status,
            metadata = timeEntry.metadata,
            createdAt = timeEntry.createdAt,
            updatedAt = timeEntry.updatedAt
        )
    }
}
