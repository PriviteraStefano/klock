package org.stefanoprivitera.klock.repository.impl

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.UserDepartment
import org.stefanoprivitera.klock.domain.UserDepartmentRequest
import org.stefanoprivitera.klock.persistance.DepartmentUsers
import org.stefanoprivitera.klock.repository.UserDepartmentRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
@OptIn(ExperimentalUuidApi::class)
class UserDepartmentRepositoryImpl : UserDepartmentRepository {
    override fun create(userDepartment: UserDepartmentRequest.Create): Uuid {
        return transaction {
            DepartmentUsers.insertAndGetId {
                it[userId] = userDepartment.userId
                it[departmentId] = userDepartment.departmentId
            }.value
        }
    }

    override fun findAll(filter: UserDepartmentRequest.Filter): List<UserDepartment> {
        return transaction {
            var query = DepartmentUsers.selectAll()

            filter.userId?.let { query = query.where { DepartmentUsers.userId eq it } }
            filter.departmentId?.let { query = query.where { DepartmentUsers.departmentId eq it } }

            query.map(::toUserDepartment)
        }
    }

    override fun deleteById(id: Uuid): Int {
        return transaction {
            DepartmentUsers.deleteWhere { DepartmentUsers.id eq id }
        }
    }

    override fun deleteByUserAndDepartment(userId: Uuid, departmentId: Uuid): Int {
        return transaction {
            DepartmentUsers.deleteWhere {
                DepartmentUsers.userId eq userId
            }
            DepartmentUsers.deleteWhere {
                DepartmentUsers.departmentId eq departmentId
            }
        }
    }

    private fun toUserDepartment(r: ResultRow): UserDepartment {
        return UserDepartment(
            id = r[DepartmentUsers.id].value,
            departmentId = r[DepartmentUsers.departmentId].value,
            userId = r[DepartmentUsers.userId].value
        )
    }
}
