package org.stefanoprivitera.klock.persistance

import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.date
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object Approvals : UuidTable("approvals") {
    val pmId = reference("pm_id", Users)
    val timeEntryId = reference("time_entry_id", TimeEntries)
    val status = varchar("status", 50) // "pending", "approved", "rejected"
    val approvalDate = date("approval_date")
}