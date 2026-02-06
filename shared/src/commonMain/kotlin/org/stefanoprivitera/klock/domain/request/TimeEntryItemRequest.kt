package org.stefanoprivitera.klock.domain.request

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.ProjectId
import org.stefanoprivitera.klock.domain.TimeEntryId
import org.stefanoprivitera.klock.domain.TimeEntryItemId

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
