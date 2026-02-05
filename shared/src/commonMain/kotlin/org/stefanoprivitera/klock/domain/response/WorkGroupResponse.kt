package org.stefanoprivitera.klock.domain.response

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.*
import kotlin.uuid.ExperimentalUuidApi

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class WorkGroupResponse(
    val id: WorkGroupId,
    val name: String,
    val description: String
) {
    companion object {
        fun from(workGroup: WorkGroup): WorkGroupResponse = WorkGroupResponse(
            id = workGroup.id,
            name = workGroup.name,
            description = workGroup.description
        )
    }
}
