package org.stefanoprivitera.klock.domain.request

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.domain.WorkGroupId

sealed interface WorkGroupUserRequest {
    @Serializable
    data class Create(
        val workGroupId: WorkGroupId,
        val userId: UserId
    ) : WorkGroupUserRequest

    @Serializable
    data class Filter(
        val workGroupId: WorkGroupId?,
        val userId: UserId?
    ) : WorkGroupUserRequest
}
