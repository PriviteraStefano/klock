package org.stefanoprivitera.klock.domain.response

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.*
import kotlin.uuid.ExperimentalUuidApi

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class DepartmentResponse(
    val id: DepartmentId,
    val name: String,
    val description: String,
    val parentDepartmentId: DepartmentId?
) {
    companion object {
        fun from(department: Department): DepartmentResponse = DepartmentResponse(
            id = department.id,
            name = department.name,
            description = department.description,
            parentDepartmentId = department.parentDepartmentId
        )
    }
}
