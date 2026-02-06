package org.stefanoprivitera.klock.domain.request

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.DepartmentId

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
