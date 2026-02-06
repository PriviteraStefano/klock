package org.stefanoprivitera.klock.domain.request

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.TimeEntryId
import org.stefanoprivitera.klock.domain.TimeEntryMetadata
import org.stefanoprivitera.klock.domain.UserId

sealed interface TimeEntryRequest {
    @Serializable
    data class Create(
        val userId: UserId,
        val date: LocalDate,
        val type: String,
        val totalHours: Double,
        val status: String,
        val metadata: TimeEntryMetadata
    ) : TimeEntryRequest

    @Serializable
    data class Update(
        val id: TimeEntryId,
        val date: LocalDate?,
        val type: String?,
        val totalHours: Double?,
        val status: String?,
        val metadata: TimeEntryMetadata?
    ) : TimeEntryRequest

    @Serializable
    data class Filter(
        val userId: UserId?,
        val dateFrom: LocalDate?,
        val dateTo: LocalDate?,
        val type: String?,
        val status: String?
    ) : TimeEntryRequest
}
