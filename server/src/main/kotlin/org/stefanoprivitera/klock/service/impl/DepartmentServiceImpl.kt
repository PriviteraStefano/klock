package org.stefanoprivitera.klock.service.impl

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Department
import org.stefanoprivivitera.klock.domain.DepartmentId
import org.stefanoprivitera.klock.domain.DepartmentRequest
import org.stefanoprivitera.klock.repository.DepartmentRepository
import org.stefanoprivitera.klock.service.DepartmentService

@Single
class DepartmentServiceImpl(
    private val departmentRepository: DepartmentRepository
) : DepartmentService {
    override fun create(department: DepartmentRequest.Create): DepartmentId {
        return departmentRepository.create(department)
    }

    override fun findById(id: DepartmentId): Department? {
        return departmentRepository.findById(id)
    }

    override fun findAll(filter: DepartmentRequest.Filter): List<Department> {
        return departmentRepository.findAll(filter)
    }

    override fun update(department: DepartmentRequest.Update): Int {
        return departmentRepository.update(department)
    }

    override fun deleteById(id: DepartmentId): Int {
        return departmentRepository.deleteById(id)
    }
}
