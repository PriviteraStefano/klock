package org.stefanoprivitera.klock.domain.request

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.*

sealed interface ProjectRequest {
    @Serializable
    data class Create(
        val name: String,
        val customerId: CustomerId,
        val managerId: UserId,
        val departmentId: DepartmentId,
        val workGroupId: WorkGroupId,
        val active: Boolean = true
    ) : ProjectRequest

    @Serializable
    data class Update(
        val id: ProjectId,
        val name: String?,
        val customerId: CustomerId?,
        val managerId: UserId?,
        val departmentId: DepartmentId?,
        val workGroupId: WorkGroupId?,
        val active: Boolean?
    ) : ProjectRequest

    @Serializable
    data class Filter(
        val name: String?,
        val customerId: CustomerId?,
        val managerId: UserId?,
        val departmentId: DepartmentId?,
        val workGroupId: WorkGroupId?,
        val active: Boolean?
    ) : ProjectRequest
}
