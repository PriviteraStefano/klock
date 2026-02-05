package org.stefanoprivitera.klock.domain.response

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.*
import kotlin.uuid.ExperimentalUuidApi

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class ProjectResponse(
    val id: ProjectId,
    val name: String,
    val customerId: CustomerId,
    val managerId: UserId,
    val departmentId: DepartmentId,
    val workGroupId: WorkGroupId,
    val active: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(project: Project): ProjectResponse = ProjectResponse(
            id = project.id,
            name = project.name,
            customerId = project.customerId,
            managerId = project.managerId,
            departmentId = project.departmentId,
            workGroupId = project.workGroupId,
            active = project.active,
            createdAt = project.createdAt,
            updatedAt = project.updatedAt
        )
    }
}
