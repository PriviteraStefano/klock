package org.stefanoprivitera.klock.domain

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TimeEntryItem(
    val id: Uuid,
    val timeEntryId: Uuid,
    val projectId: Uuid,
    val hours: Double,
    val approved: Boolean
)

sealed interface TimeEntryItemRequest {
    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Create(
        val timeEntryId: Uuid,
        val projectId: Uuid,
        val hours: Double
    ) : TimeEntryItemRequest

    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Update(
        val id: Uuid,
        val projectId: Uuid?,
        val hours: Double?,
        val approved: Boolean?
    ) : TimeEntryItemRequest

    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Filter(
        val timeEntryId: Uuid?,
        val projectId: Uuid?,
        val approved: Boolean?
    ) : TimeEntryItemRequest
}
