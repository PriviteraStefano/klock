package org.stefanoprivitera.klock.domain.request

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.WorkGroupId

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
