package org.stefanoprivitera.klock.domain

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TimeEntryItem(
    val id: Uuid,
    val timeEntryId: Uuid,
    val projectId: Uuid,
    val hours: Double
)
