package org.stefanoprivitera.klock.domain.response

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.*
import kotlin.uuid.ExperimentalUuidApi

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class UserDepartmentResponse(
    val id: UserDepartmentId,
    val departmentId: DepartmentId,
    val userId: UserId
) {
    companion object {
        fun from(userDepartment: UserDepartment): UserDepartmentResponse = UserDepartmentResponse(
            id = userDepartment.id,
            departmentId = userDepartment.departmentId,
            userId = userDepartment.userId
        )
    }
}
