package org.stefanoprivitera.klock.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JvmInline
@Serializable
value class ProjectId @OptIn(ExperimentalUuidApi::class) constructor(val value: Uuid)

@OptIn(ExperimentalUuidApi::class)
data class Project(
    val id: ProjectId,
    val name: String,
    val customerId: CustomerId,
    val managerId: UserId,
    val departmentId: DepartmentId,
    val workGroupId: WorkGroupId,
    val active: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

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