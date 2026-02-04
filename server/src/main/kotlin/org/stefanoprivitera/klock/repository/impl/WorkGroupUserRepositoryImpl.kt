package org.stefanoprivitera.klock.repository.impl

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.WorkGroupUser
import org.stefanoprivitera.klock.domain.WorkGroupUserRequest
import org.stefanoprivitera.klock.persistance.WorkGroupUsers
import org.stefanoprivitera.klock.repository.WorkGroupUserRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
@OptIn(ExperimentalUuidApi::class)
class WorkGroupUserRepositoryImpl : WorkGroupUserRepository {
    override fun create(workGroupUser: WorkGroupUserRequest.Create): Uuid {
        return transaction {
            WorkGroupUsers.insertAndGetId {
                it[workGroupId] = workGroupUser.workGroupId
                it[userId] = workGroupUser.userId
            }.value
        }
    }

    override fun findAll(filter: WorkGroupUserRequest.Filter): List<WorkGroupUser> {
        return transaction {
            var query = WorkGroupUsers.selectAll()

            filter.workGroupId?.let { query = query.where { WorkGroupUsers.workGroupId eq it } }
            filter.userId?.let { query = query.where { WorkGroupUsers.userId eq it } }

            query.map(::toWorkGroupUser)
        }
    }

    override fun deleteById(id: Uuid): Int {
        return transaction {
            WorkGroupUsers.deleteWhere { WorkGroupUsers.id eq id }
        }
    }

    override fun deleteByUserAndWorkGroup(userId: Uuid, workGroupId: Uuid): Int {
        return transaction {
            WorkGroupUsers.deleteWhere {
                WorkGroupUsers.userId eq userId
            }
            WorkGroupUsers.deleteWhere {
                WorkGroupUsers.workGroupId eq workGroupId
            }
        }
    }

    private fun toWorkGroupUser(r: ResultRow): WorkGroupUser {
        return WorkGroupUser(
            id = r[WorkGroupUsers.id].value,
            workGroupId = r[WorkGroupUsers.workGroupId].value,
            userId = r[WorkGroupUsers.userId].value
        )
    }
}
