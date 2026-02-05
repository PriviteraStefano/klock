package org.stefanoprivitera.klock.repository.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import org.stefanoprivitera.klock.domain.ProjectId
import org.stefanoprivitera.klock.domain.TimeEntryId
import org.stefanoprivitera.klock.domain.TimeEntryItem
import org.stefanoprivitera.klock.domain.TimeEntryItemId
import org.stefanoprivitera.klock.persistance.TimeEntryItems
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun toTimeEntryItem(r: ResultRow): TimeEntryItem {
    return TimeEntryItem(
        id = TimeEntryItemId(r[TimeEntryItems.id].value),
        timeEntryId = TimeEntryId(r[TimeEntryItems.timeEntryId].value),
        projectId = ProjectId(r[TimeEntryItems.projectId].value),
        hours = r[TimeEntryItems.hours].toDouble(),
        approved = r[TimeEntryItems.approved]
    )
}