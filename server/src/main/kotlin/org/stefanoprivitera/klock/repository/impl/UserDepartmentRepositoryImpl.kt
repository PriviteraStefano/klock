package org.stefanoprivitera.klock.repository.impl

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.*
import org.stefanoprivitera.klock.domain.request.UserDepartmentRequest
import org.stefanoprivitera.klock.persistance.DepartmentUsers
import org.stefanoprivitera.klock.repository.UserDepartmentRepository
import org.stefanoprivitera.klock.repository.utils.andWhereIfNotNull
import kotlin.uuid.ExperimentalUuidApi

@Single
@OptIn(ExperimentalUuidApi::class)
class UserDepartmentRepositoryImpl : UserDepartmentRepository {
    override fun create(userDepartment: UserDepartmentRequest.Create): UserDepartmentId {
        return transaction {
            UserDepartmentId(DepartmentUsers.insertAndGetId {
                it[userId] = userDepartment.userId.value
                it[departmentId] = userDepartment.departmentId.value
            }.value)
        }
    }

    override fun findAll(filter: UserDepartmentRequest.Filter): List<UserDepartment> {
        return transaction {
            DepartmentUsers.selectAll()
                .andWhereIfNotNull(filter.userId) { DepartmentUsers.userId eq it.value }
                .andWhereIfNotNull(filter.departmentId) { DepartmentUsers.departmentId eq it.value }
                .map(::toUserDepartment)
        }
    }

    override fun deleteById(id: UserDepartmentId): Int {
        return transaction {
            DepartmentUsers.deleteWhere { DepartmentUsers.id eq id.value }
        }
    }

    override fun deleteDepartmentUser(userId: UserId, departmentId: DepartmentId): Int {
        return transaction {
            DepartmentUsers.deleteWhere {
                DepartmentUsers.userId eq userId.value and(DepartmentUsers.departmentId eq departmentId.value)
            }
        }
    }

    private fun toUserDepartment(r: ResultRow): UserDepartment {
        return UserDepartment(
            id = UserDepartmentId(r[DepartmentUsers.id].value),
            departmentId = DepartmentId(r[DepartmentUsers.departmentId].value),
            userId = UserId(r[DepartmentUsers.userId].value)
        )
    }
}
