package org.stefanoprivitera.klock.domain.response

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.*
import kotlin.uuid.ExperimentalUuidApi

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class WorkGroupUserResponse(
    val id: WorkGroupUserId,
    val workGroupId: WorkGroupId,
    val userId: UserId
) {
    companion object {
        fun from(workGroupUser: WorkGroupUser): WorkGroupUserResponse = WorkGroupUserResponse(
            id = workGroupUser.id,
            workGroupId = workGroupUser.workGroupId,
            userId = workGroupUser.userId
        )
    }
}
