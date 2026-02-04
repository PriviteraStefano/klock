package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.UserDepartment
import org.stefanoprivitera.klock.domain.UserDepartmentRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface UserDepartmentRepository {
    fun create(userDepartment: UserDepartmentRequest.Create): Uuid
    fun findAll(filter: UserDepartmentRequest.Filter): List<UserDepartment>
    fun deleteById(id: Uuid): Int
    fun deleteByUserAndDepartment(userId: Uuid, departmentId: Uuid): Int
}
