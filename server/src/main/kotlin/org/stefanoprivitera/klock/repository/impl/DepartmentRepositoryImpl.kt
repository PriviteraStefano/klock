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
import org.stefanoprivitera.klock.domain.Department
import org.stefanoprivitera.klock.domain.DepartmentRequest
import org.stefanoprivitera.klock.domain.User
import org.stefanoprivitera.klock.persistance.DepartmentUsers
import org.stefanoprivitera.klock.persistance.Departments
import org.stefanoprivitera.klock.persistance.Users
import org.stefanoprivitera.klock.repository.DepartmentRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
@OptIn(ExperimentalUuidApi::class)
class DepartmentRepositoryImpl : DepartmentRepository {
    override fun create(department: DepartmentRequest.Create): String {
        return transaction {
            Departments.insertAndGetId {
                it[name] = department.name
                it[description] = department.description
                it[parentDepartmentId] = department.parentDepartmentId
            }.value.toString()
        }
    }

    override fun update(department: DepartmentRequest.Update): Int {
        return transaction {
            Departments.update({ Departments.id eq department.id }) {
                department.name?.let { n -> it[name] = n }
                department.description?.let { d -> it[description] = d }
                department.parentDepartmentId?.let { p -> it[parentDepartmentId] = p }
            }
        }
    }

    override fun findById(id: String): Department? {
        return transaction {
            val uuid = Uuid.parse(id)
            Departments.selectAll()
                .where { Departments.id eq uuid }
                .map { departmentRow ->
                    toDepartment(departmentRow, uuid)
                }
                .firstOrNull()
        }
    }

    override fun findAll(filter: DepartmentRequest.Filter): List<Department> {
        return transaction {
            var query = Departments.selectAll()

            filter.name?.let {
                query = query.where { Departments.name like "%$it%" }
            }
            filter.description?.let {
                query = query.where { Departments.description like "%$it%" }
            }
            filter.parentDepartmentId?.let {
                query = query.where { Departments.parentDepartmentId eq it }
            }

            query.map { departmentRow ->
                toDepartment(departmentRow, departmentRow[Departments.id].value)
            }
        }
    }

    override fun deleteById(id: String): Int {
        return transaction {
            val uuid = Uuid.parse(id)
            Departments.deleteWhere { Departments.id eq uuid }
        }
    }

    private fun toDepartment(departmentRow: ResultRow, departmentId: Uuid): Department {
        // Fetch users associated with this department
        val users = transaction {
            (DepartmentUsers innerJoin Users)
                .selectAll()
                .where { DepartmentUsers.departmentId eq departmentId }
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

        return Department(
            id = departmentRow[Departments.id].value,
            name = departmentRow[Departments.name],
            description = departmentRow[Departments.description],
            parentDepartmentId = departmentRow[Departments.parentDepartmentId]?.value,
            departmentUsers = users
        )
    }
}
