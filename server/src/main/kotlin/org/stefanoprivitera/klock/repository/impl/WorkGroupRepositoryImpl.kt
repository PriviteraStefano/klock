package org.stefanoprivitera.klock.repository.impl

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.User
import org.stefanoprivitera.klock.domain.WorkGroup
import org.stefanoprivitera.klock.domain.WorkGroupRequest
import org.stefanoprivitera.klock.persistance.Users
import org.stefanoprivitera.klock.persistance.WorkGroupUsers
import org.stefanoprivitera.klock.persistance.WorkGroups
import org.stefanoprivitera.klock.repository.WorkGroupRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
@OptIn(ExperimentalUuidApi::class)
class WorkGroupRepositoryImpl : WorkGroupRepository {
    override fun create(workGroup: WorkGroupRequest.Create): Uuid {
        return transaction {
            WorkGroups.insertAndGetId {
                it[name] = workGroup.name
                it[description] = workGroup.description
            }.value
        }
    }

    override fun update(workGroup: WorkGroupRequest.Update): Int {
        return transaction {
            WorkGroups.update({ WorkGroups.id eq workGroup.id }) {
                workGroup.name?.let { n -> it[name] = n }
                workGroup.description?.let { d -> it[description] = d }
            }
        }
    }

    override fun findById(id: Uuid): WorkGroup? {
        return transaction {
            WorkGroups.selectAll()
                .where { WorkGroups.id eq id }
                .map { workGroupRow ->
                    toWorkGroup(workGroupRow, id)
                }
                .firstOrNull()
        }
    }

    override fun findAll(filter: WorkGroupRequest.Filter): List<WorkGroup> {
        return transaction {
            var query = WorkGroups.selectAll()

            filter.name?.let {
                query = query.where { WorkGroups.name like "%$it%" }
            }
            filter.description?.let {
                query = query.where { WorkGroups.description like "%$it%" }
            }

            query.map { workGroupRow ->
                toWorkGroup(workGroupRow, workGroupRow[WorkGroups.id].value)
            }
        }
    }

    override fun deleteById(id: Uuid): Int {
        return transaction {
            WorkGroups.deleteWhere { WorkGroups.id eq id }
        }
    }

    private fun toWorkGroup(workGroupRow: ResultRow, workGroupId: Uuid): WorkGroup {
        // Fetch users associated with this work group
        val users = transaction {
            (WorkGroupUsers innerJoin Users)
                .selectAll()
                .where { WorkGroupUsers.workGroupId eq workGroupId }
                .map { userRow ->
                    User(
                        id = userRow[Users.id].value,
                        email = userRow[Users.email],
                        firstname = userRow[Users.firstname],
                        lastname = userRow[Users.lastname],
                        createdAt = userRow[Users.createdAt],
                        updatedAt = userRow[Users.updatedAt]
                    )
                }
        }

        return WorkGroup(
            id = workGroupRow[WorkGroups.id].value,
            name = workGroupRow[WorkGroups.name],
            description = workGroupRow[WorkGroups.description] ?: "",
            users = users
        )
    }
}
