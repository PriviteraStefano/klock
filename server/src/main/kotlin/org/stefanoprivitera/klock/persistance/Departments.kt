package org.stefanoprivitera.klock.persistance

import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object Departments : UuidTable("departments") {
    val name = varchar("name", 255).uniqueIndex()
    val description = text("description")
    val parentDepartmentId = reference("parent_department_id", Departments).nullable()
}
