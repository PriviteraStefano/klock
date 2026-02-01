package org.stefanoprivitera.klock.domain

import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Approval(
    val id: Uuid,
    val pmId: Uuid,
    val timeEntryId: Uuid,
    val status: String, // "pending", "approved", "rejected"
    val approvalDate: LocalDate
)
