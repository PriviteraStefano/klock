package org.stefanoprivitera.klock.repository.impl

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.WorkGroup
import org.stefanoprivitera.klock.domain.WorkGroupId
import org.stefanoprivitera.klock.domain.WorkGroupRequest
import org.stefanoprivitera.klock.persistance.WorkGroups
import org.stefanoprivitera.klock.repository.WorkGroupRepository
import org.stefanoprivitera.klock.repository.mapper.toWorkGroup
import org.stefanoprivitera.klock.repository.utils.andWhereIfNotNull
import kotlin.uuid.ExperimentalUuidApi

@Single
@OptIn(ExperimentalUuidApi::class)
class WorkGroupRepositoryImpl : WorkGroupRepository {
    override fun create(workGroup: WorkGroupRequest.Create): WorkGroupId {
        return transaction {
            WorkGroupId(WorkGroups.insertAndGetId {
                it[name] = workGroup.name
                it[description] = workGroup.description
            }.value)
        }
    }

    override fun update(workGroup: WorkGroupRequest.Update): Int {
        return transaction {
            WorkGroups.update({ WorkGroups.id eq workGroup.id.value }) {
                workGroup.name?.let { n -> it[name] = n }
                workGroup.description?.let { d -> it[description] = d }
            }
        }
    }

    override fun findById(id: WorkGroupId): WorkGroup? {
        return transaction {
            WorkGroups.selectAll()
                .where { WorkGroups.id eq id.value }
                .map(::toWorkGroup)
                .firstOrNull()
        }
    }

    override fun findAll(filter: WorkGroupRequest.Filter): List<WorkGroup> {
        return transaction {
            WorkGroups.selectAll()
                .andWhereIfNotNull(filter.name) { WorkGroups.name eq it }
                .andWhereIfNotNull(filter.description) { WorkGroups.description eq it }
                .map(::toWorkGroup)
        }
    }

    override fun deleteById(id: WorkGroupId): Int {
        return transaction {
            WorkGroups.deleteWhere { WorkGroups.id eq id.value }
        }
    }

}
