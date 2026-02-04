package org.stefanoprivitera.klock.repository.impl

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Project
import org.stefanoprivitera.klock.domain.ProjectRequest
import org.stefanoprivitera.klock.persistance.Projects
import org.stefanoprivitera.klock.repository.ProjectRepository
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
@OptIn(ExperimentalUuidApi::class)
class ProjectRepositoryImpl : ProjectRepository {
    override fun create(project: ProjectRequest.Create): String {
        return transaction {
            Projects.insertAndGetId {
                it[name] = project.name
                it[customerId] = project.customerId
                it[managerId] = project.managerId
                it[departmentId] = project.departmentId
                it[workGroupId] = project.workGroupId
                it[active] = project.active
                it[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.value.toString()
        }
    }

    override fun update(project: ProjectRequest.Update): Int {
        return transaction {
            Projects.update({ Projects.id eq project.id }) {
                project.name?.let { n -> it[name] = n }
                project.customerId?.let { c -> it[customerId] = c }
                project.managerId?.let { m -> it[managerId] = m }
                project.departmentId?.let { d -> it[departmentId] = d }
                project.workGroupId?.let { w -> it[workGroupId] = w }
                project.active?.let { a -> it[active] = a }
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }
        }
    }

    override fun findById(id: Uuid): Project? {
        return transaction {
            Projects.selectAll()
                .where { Projects.id eq id }
                .map(::toProject)
                .firstOrNull()
        }
    }

    override fun findAll(filter: ProjectRequest.Filter): List<Project> {
        return transaction {
            var query = Projects.selectAll()

            filter.name?.let {
                query = query.where { Projects.name like "%$it%" }
            }
            filter.customerId?.let {
                query = query.where { Projects.customerId eq it }
            }
            filter.managerId?.let {
                query = query.where { Projects.managerId eq it }
            }
            filter.departmentId?.let {
                query = query.where { Projects.departmentId eq it }
            }
            filter.workGroupId?.let {
                query = query.where { Projects.workGroupId eq it }
            }
            filter.active?.let {
                query = query.where { Projects.active eq it }
            }

            query.map(::toProject)
        }
    }

    override fun deleteById(id: Uuid): Int {
        return transaction {
            Projects.deleteWhere { Projects.id eq id }
        }
    }

    private fun toProject(r: ResultRow): Project {
        return Project(
            id = r[Projects.id].value,
            name = r[Projects.name],
            customerId = r[Projects.customerId].value,
            managerId = r[Projects.managerId].value,
            departmentId = r[Projects.departmentId].value,
            workGroupId = r[Projects.workGroupId].value,
            active = r[Projects.active],
            createdAt = r[Projects.createdAt],
            updatedAt = r[Projects.updatedAt]
        )
    }
}
