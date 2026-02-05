package org.stefanoprivitera.klock.service.impl

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.DepartmentId
import org.stefanoprivitera.klock.domain.UserDepartment
import org.stefanoprivitera.klock.domain.UserDepartmentId
import org.stefanoprivitera.klock.domain.UserDepartmentRequest
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.repository.UserDepartmentRepository
import org.stefanoprivitera.klock.service.UserDepartmentService

@Single
class UserDepartmentServiceImpl(
    private val userDepartmentRepository: UserDepartmentRepository
) : UserDepartmentService {
    override fun create(userDepartment: UserDepartmentRequest.Create): UserDepartmentId {
        return userDepartmentRepository.create(userDepartment)
    }

    override fun findAll(filter: UserDepartmentRequest.Filter): List<UserDepartment> {
        return userDepartmentRepository.findAll(filter)
    }

    override fun deleteById(id: UserDepartmentId): Int {
        return userDepartmentRepository.deleteById(id)
    }

    override fun deleteByUserAndDepartment(userId: UserId, departmentId: DepartmentId): Int {
        return userDepartmentRepository.deleteDepartmentUser(userId, departmentId)
    }
}
