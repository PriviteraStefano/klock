package org.stefanoprivitera.klock.domain

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JvmInline
@Serializable
value class DepartmentId @OptIn(ExperimentalUuidApi::class) constructor(val value: Uuid)

@OptIn(ExperimentalUuidApi::class)
data class Department(
    val id: DepartmentId,
    val name: String,
    val description: String,
    val parentDepartmentId: DepartmentId?,
)

sealed interface DepartmentRequest {
    @Serializable
    data class Create(
        val name: String,
        val description: String,
        val parentDepartmentId: DepartmentId?
    ) : DepartmentRequest

    @Serializable
    data class Update(
        val id: DepartmentId,
        val name: String?,
        val description: String?,
        val parentDepartmentId: DepartmentId?
    ) : DepartmentRequest

    @Serializable
    data class Filter(
        val name: String?,
        val description: String?,
        val parentDepartmentId: DepartmentId?
    ) : DepartmentRequest
}