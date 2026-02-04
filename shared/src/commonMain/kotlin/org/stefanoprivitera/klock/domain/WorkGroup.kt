package org.stefanoprivitera.klock.domain

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class WorkGroup (
    val id: Uuid,
    val name: String,
    val description: String,
    val users: List<User>
)

sealed interface WorkGroupRequest {
    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Create(
        val name: String,
        val description: String
    ) : WorkGroupRequest

    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Update(
        val id: Uuid,
        val name: String?,
        val description: String?
    ) : WorkGroupRequest

    @Serializable
    data class Filter(
        val name: String?,
        val description: String?,
        val users: List<String>? // user IDs
    ) : WorkGroupRequest
}
