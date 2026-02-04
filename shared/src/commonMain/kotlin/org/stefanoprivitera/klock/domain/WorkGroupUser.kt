package org.stefanoprivitera.klock.domain

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class WorkGroupUser(
    val id: Uuid,
    val workGroupId: Uuid,
    val userId: Uuid
)

sealed interface WorkGroupUserRequest {
    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Create(
        val workGroupId: Uuid,
        val userId: Uuid
    ) : WorkGroupUserRequest

    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Filter(
        val workGroupId: Uuid?,
        val userId: Uuid?
    ) : WorkGroupUserRequest
}
