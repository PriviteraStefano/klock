package org.stefanoprivitera.klock.domain

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JvmInline
@Serializable
value class TimeEntryItemId @OptIn(ExperimentalUuidApi::class) constructor(val value: Uuid)

@OptIn(ExperimentalUuidApi::class)
data class TimeEntryItem(
    val id: TimeEntryItemId,
    val timeEntryId: TimeEntryId,
    val projectId: ProjectId,
    val hours: Double,
    val approved: Boolean
)

sealed interface TimeEntryItemRequest {
    @Serializable
    data class Create(
        val timeEntryId: TimeEntryId,
        val projectId: ProjectId,
        val hours: Double
    ) : TimeEntryItemRequest

    @Serializable
    data class Update(
        val id: TimeEntryItemId,
        val projectId: ProjectId?,
        val hours: Double?,
        val approved: Boolean?
    ) : TimeEntryItemRequest

    @Serializable
    data class Filter(
        val timeEntryId: TimeEntryId?,
        val projectId: ProjectId?,
        val approved: Boolean?
    ) : TimeEntryItemRequest
}
