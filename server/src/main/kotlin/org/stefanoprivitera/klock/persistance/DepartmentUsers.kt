package org.stefanoprivitera.klock.persistance

import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object DepartmentUsers : UuidTable("user_departments") {
    val userId = reference("user_id", Users)
    val departmentId = reference("department_id", Departments)

    init {
        uniqueIndex(userId, departmentId)
    }
}
