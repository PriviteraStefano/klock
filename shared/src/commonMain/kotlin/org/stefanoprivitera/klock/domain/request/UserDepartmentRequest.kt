package org.stefanoprivitera.klock.domain.request

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.DepartmentId
import org.stefanoprivitera.klock.domain.UserId

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
