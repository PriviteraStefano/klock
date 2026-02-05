package org.stefanoprivitera.klock.service

import org.stefanoprivitera.klock.domain.DepartmentId
import org.stefanoprivitera.klock.domain.UserDepartment
import org.stefanoprivitera.klock.domain.UserDepartmentId
import org.stefanoprivitera.klock.domain.UserDepartmentRequest
import org.stefanoprivitera.klock.domain.UserId

interface UserDepartmentService {
    fun create(userDepartment: UserDepartmentRequest.Create): UserDepartmentId
    fun findAll(filter: UserDepartmentRequest.Filter): List<UserDepartment>
    fun deleteById(id: UserDepartmentId): Int
    fun deleteByUserAndDepartment(userId: UserId, departmentId: DepartmentId): Int
}
