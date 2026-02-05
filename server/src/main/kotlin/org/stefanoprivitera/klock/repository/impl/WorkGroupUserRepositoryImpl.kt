package org.stefanoprivitera.klock.repository.impl

import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.*
import org.stefanoprivitera.klock.persistance.WorkGroupUsers
import org.stefanoprivitera.klock.repository.WorkGroupUserRepository
import org.stefanoprivitera.klock.repository.mapper.toWorkGroupUser
import kotlin.uuid.ExperimentalUuidApi

@Single
@OptIn(ExperimentalUuidApi::class)
class WorkGroupUserRepositoryImpl : WorkGroupUserRepository {
    override fun create(workGroupUser: WorkGroupUserRequest.Create): WorkGroupUserId {
        return transaction {
            WorkGroupUserId(WorkGroupUsers.insertAndGetId {
                it[workGroupId] = workGroupUser.workGroupId.value
                it[userId] = workGroupUser.userId.value
            }.value)
        }
    }

    override fun findAll(filter: WorkGroupUserRequest.Filter): List<WorkGroupUser> {
        return transaction {
            var query = WorkGroupUsers.selectAll()

            filter.workGroupId?.let { query = query.where { WorkGroupUsers.workGroupId eq it.value } }
            filter.userId?.let { query = query.where { WorkGroupUsers.userId eq it.value } }

            query.map(::toWorkGroupUser)
        }
    }

    override fun deleteById(id: WorkGroupUserId): Int {
        return transaction {
            WorkGroupUsers.deleteWhere { WorkGroupUsers.id eq id.value }
        }
    }

    override fun deleteWorkGroupUser(userId: UserId, workGroupId: WorkGroupId): Int {
        return transaction {
            WorkGroupUsers.deleteWhere {
                WorkGroupUsers.userId eq userId.value and(WorkGroupUsers.workGroupId eq workGroupId.value)
            }
        }
    }

}
