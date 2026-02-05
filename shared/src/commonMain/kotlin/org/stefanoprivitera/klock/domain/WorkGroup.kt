package org.stefanoprivitera.klock.domain

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JvmInline
@Serializable
value class WorkGroupId @OptIn(ExperimentalUuidApi::class) constructor(val value: Uuid)

@OptIn(ExperimentalUuidApi::class)
data class WorkGroup (
    val id: WorkGroupId,
    val name: String,
    val description: String,
)

sealed interface WorkGroupRequest {
    @Serializable
    data class Create(
        val name: String,
        val description: String
    ) : WorkGroupRequest

    @Serializable
    data class Update(
        val id: WorkGroupId,
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
