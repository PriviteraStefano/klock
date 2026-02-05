package org.stefanoprivitera.klock.service

import org.stefanoprivitera.klock.domain.Department
import org.stefanoprivitera.klock.domain.DepartmentId
import org.stefanoprivitera.klock.domain.DepartmentRequest

interface DepartmentService {
    fun create(department: DepartmentRequest.Create): DepartmentId
    fun findById(id: DepartmentId): Department?
    fun findAll(filter: DepartmentRequest.Filter): List<Department>
    fun update(department: DepartmentRequest.Update): Int
    fun deleteById(id: DepartmentId): Int
}