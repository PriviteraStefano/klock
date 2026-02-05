package org.stefanoprivitera.klock.domain

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.Uuid
import kotlin.uuid.ExperimentalUuidApi

@JvmInline
@Serializable
value class UserDepartmentId @OptIn(ExperimentalUuidApi::class) constructor(val value: Uuid)

@OptIn(ExperimentalUuidApi::class)
data class UserDepartment(
    val id: UserDepartmentId,
    val departmentId: DepartmentId,
    val userId: UserId
)

sealed interface UserDepartmentRequest {
    @Serializable
    data class Create(
        val userId: UserId,
        val departmentId: DepartmentId
    ) : UserDepartmentRequest

    @Serializable
    data class Filter(
        val userId: UserId?,
        val departmentId: DepartmentId?
    ) : UserDepartmentRequest
}
