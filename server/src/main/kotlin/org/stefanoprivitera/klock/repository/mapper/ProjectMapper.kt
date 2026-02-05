package org.stefanoprivitera.klock.repository.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import org.stefanoprivitera.klock.domain.CustomerId
import org.stefanoprivitera.klock.domain.DepartmentId
import org.stefanoprivitera.klock.domain.Project
import org.stefanoprivitera.klock.domain.ProjectId
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.domain.WorkGroupId
import org.stefanoprivitera.klock.persistance.Projects
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun toProject(r: ResultRow): Project {
    return Project(
        id = ProjectId(r[Projects.id].value),
        name = r[Projects.name],
        customerId = CustomerId(r[Projects.customerId].value),
        managerId = UserId(r[Projects.managerId].value),
        departmentId = DepartmentId(r[Projects.departmentId].value),
        workGroupId = WorkGroupId(r[Projects.workGroupId].value),
        active = r[Projects.active],
        createdAt = r[Projects.createdAt],
        updatedAt = r[Projects.updatedAt]
    )
}