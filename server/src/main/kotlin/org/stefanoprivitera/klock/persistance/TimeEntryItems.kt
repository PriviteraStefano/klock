package org.stefanoprivitera.klock.persistance

import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object TimeEntryItems : UuidTable("time_entry_items") {
    val timeEntryId = reference("time_entry_id", TimeEntries)
    val projectId = reference("project_id", Projects)
    val hours = decimal("hours", 5, 2)
    val approved = bool("approved").default(false)
}