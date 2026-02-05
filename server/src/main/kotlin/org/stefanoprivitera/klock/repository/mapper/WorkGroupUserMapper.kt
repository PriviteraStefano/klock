package org.stefanoprivitera.klock.repository.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.domain.WorkGroupId
import org.stefanoprivitera.klock.domain.WorkGroupUser
import org.stefanoprivitera.klock.domain.WorkGroupUserId
import org.stefanoprivitera.klock.persistance.WorkGroupUsers
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun toWorkGroupUser(r: ResultRow): WorkGroupUser {
    return WorkGroupUser(
        id = WorkGroupUserId(r[WorkGroupUsers.id].value),
        workGroupId = WorkGroupId(r[WorkGroupUsers.workGroupId].value),
        userId = UserId(r[WorkGroupUsers.userId].value)
    )
}