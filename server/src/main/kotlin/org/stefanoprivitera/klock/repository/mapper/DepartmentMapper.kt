package org.stefanoprivitera.klock.repository.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import org.stefanoprivitera.klock.domain.Department
import org.stefanoprivitera.klock.domain.DepartmentId
import org.stefanoprivitera.klock.persistance.Departments
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun toDepartment(departmentRow: ResultRow): Department {
    return Department(
        id = DepartmentId(departmentRow[Departments.id].value),
        name = departmentRow[Departments.name],
        description = departmentRow[Departments.description],
        parentDepartmentId = departmentRow[Departments.parentDepartmentId]?.value?.let { DepartmentId(it) },
    )
}