package org.stefanoprivitera.klock.repository.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import org.stefanoprivitera.klock.domain.WorkGroup
import org.stefanoprivitera.klock.domain.WorkGroupId
import org.stefanoprivitera.klock.persistance.WorkGroups
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun toWorkGroup(workGroupRow: ResultRow): WorkGroup {
    // Fetch users associated with this work group
    return WorkGroup(
        id = WorkGroupId(workGroupRow[WorkGroups.id].value),
        name = workGroupRow[WorkGroups.name],
        description = workGroupRow[WorkGroups.description] ?: "",
    )
}