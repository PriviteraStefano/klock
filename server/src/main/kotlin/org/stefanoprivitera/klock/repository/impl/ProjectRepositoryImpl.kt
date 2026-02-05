package org.stefanoprivitera.klock.repository.impl

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.*
import org.stefanoprivitera.klock.persistance.Projects
import org.stefanoprivitera.klock.repository.ProjectRepository
import org.stefanoprivitera.klock.repository.mapper.toProject
import org.stefanoprivitera.klock.repository.utils.andWhereIfNotNull
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi

@Single
@OptIn(ExperimentalUuidApi::class)
class ProjectRepositoryImpl : ProjectRepository {
    override fun create(project: ProjectRequest.Create): ProjectId {
        return transaction {
            ProjectId(Projects.insertAndGetId {
                it[name] = project.name
                it[customerId] = project.customerId.value
                it[managerId] = project.managerId.value
                it[departmentId] = project.departmentId.value
                it[workGroupId] = project.workGroupId.value
                it[active] = project.active
                it[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.value)
        }
    }

    override fun update(project: ProjectRequest.Update): Int {
        return transaction {
            Projects.update({ Projects.id eq project.id.value }) {
                project.name?.let { n -> it[name] = n }
                project.customerId?.let { c -> it[customerId] = c.value }
                project.managerId?.let { m -> it[managerId] = m.value }
                project.departmentId?.let { d -> it[departmentId] = d.value }
                project.workGroupId?.let { w -> it[workGroupId] = w.value }
                project.active?.let { a -> it[active] = a }
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }
        }
    }

    override fun findById(id: ProjectId): Project? {
        return transaction {
            Projects.selectAll()
                .where { Projects.id eq id.value }
                .map(::toProject)
                .firstOrNull()
        }
    }

    override fun findAll(filter: ProjectRequest.Filter): List<Project> {
        return transaction {
            Projects.selectAll()
                .andWhereIfNotNull(filter.name) { Projects.name like "%${it}%" }
                .andWhereIfNotNull(filter.customerId) { Projects.customerId eq it.value }
                .andWhereIfNotNull(filter.managerId) { Projects.managerId eq it.value }
                .andWhereIfNotNull(filter.departmentId) { Projects.departmentId eq it.value }
                .andWhereIfNotNull(filter.workGroupId) { Projects.workGroupId eq it.value }
                .andWhereIfNotNull(filter.active) { Projects.active eq it }
                .map(::toProject)
        }
    }

    override fun deleteById(id: ProjectId): Int {
        return transaction {
            Projects.deleteWhere { Projects.id eq id.value }
        }
    }
}