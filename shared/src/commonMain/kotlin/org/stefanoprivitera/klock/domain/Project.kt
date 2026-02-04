package org.stefanoprivitera.klock.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Project(
    val id: Uuid,
    val name: String,
    val customerId: Uuid,
    val managerId: Uuid,
    val departmentId: Uuid,
    val workGroupId: Uuid,
    val active: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

sealed interface ProjectRequest {
    @Serializable
    @OptIn(ExperimentalUuidApi::class)
    data class Create(
        val name: String,
        val customerId: Uuid,
        val managerId: Uuid,
        val departmentId: Uuid,
        val workGroupId: Uuid,
        val active: Boolean = true
    ) : ProjectRequest

    @Serializable
    @OptIn(ExperimentalUuidApi::class)
    data class Update(
        val id: Uuid,
        val name: String?,
        val customerId: Uuid?,
        val managerId: Uuid?,
        val departmentId: Uuid?,
        val workGroupId: Uuid?,
        val active: Boolean?
    ) : ProjectRequest

    @Serializable
    @OptIn(ExperimentalUuidApi::class)
    data class Filter(
        val name: String?,
        val customerId: Uuid?,
        val managerId: Uuid?,
        val departmentId: Uuid?,
        val workGroupId: Uuid?,
        val active: Boolean?
    ) : ProjectRequest
}