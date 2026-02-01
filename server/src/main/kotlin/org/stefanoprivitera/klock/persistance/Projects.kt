package org.stefanoprivitera.klock.persistance

import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object Projects : UuidTable("projects") {
    val name = varchar("name", 255)
    val customer = varchar("customer", 255)
    val managerId = reference("manager_id", Users)
    val departmentId = reference("department_id", Departments)
    val active = bool("active").default(true)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
