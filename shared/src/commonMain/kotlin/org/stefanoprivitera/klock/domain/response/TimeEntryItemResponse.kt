package org.stefanoprivitera.klock.domain.response

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.*
import kotlin.uuid.ExperimentalUuidApi

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class TimeEntryItemResponse(
    val id: TimeEntryItemId,
    val timeEntryId: TimeEntryId,
    val projectId: ProjectId,
    val hours: Double,
    val approved: Boolean
) {
    companion object {
        fun from(item: TimeEntryItem): TimeEntryItemResponse = TimeEntryItemResponse(
            id = item.id,
            timeEntryId = item.timeEntryId,
            projectId = item.projectId,
            hours = item.hours,
            approved = item.approved
        )
    }
}
