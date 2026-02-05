package org.stefanoprivitera.klock.repository.impl

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Department
import org.stefanoprivitera.klock.domain.DepartmentId
import org.stefanoprivitera.klock.domain.DepartmentRequest
import org.stefanoprivitera.klock.persistance.Departments
import org.stefanoprivitera.klock.persistance.Users
import org.stefanoprivitera.klock.repository.DepartmentRepository
import org.stefanoprivitera.klock.repository.mapper.toDepartment
import org.stefanoprivitera.klock.repository.utils.andWhereIfNotNull
import kotlin.uuid.ExperimentalUuidApi

@Single
@OptIn(ExperimentalUuidApi::class)
class DepartmentRepositoryImpl : DepartmentRepository {
    override fun create(department: DepartmentRequest.Create): DepartmentId {
        return transaction {
            DepartmentId(Departments.insertAndGetId {
                it[name] = department.name
                it[description] = department.description
                it[parentDepartmentId] = department.parentDepartmentId?.value
            }.value)
        }
    }

    override fun update(department: DepartmentRequest.Update): Int {
        return transaction {
            Departments.update({ Departments.id eq department.id.value }) {
                department.name?.let { n -> it[name] = n }
                department.description?.let { d -> it[description] = d }
                department.parentDepartmentId?.let { p -> it[parentDepartmentId] = p.value }
            }
        }
    }

    override fun findById(id: DepartmentId): Department? {
        return transaction {
            Departments.selectAll()
                .where { Departments.id eq id.value }
                .map(::toDepartment)
                .firstOrNull()
        }
    }

    override fun findAll(filter: DepartmentRequest.Filter): List<Department> {
        return transaction {
            (Departments innerJoin Users).selectAll()
                .andWhereIfNotNull(filter.name) { Departments.name like "%$it%" }
                .andWhereIfNotNull(filter.description) { Departments.description like "%$it%" }
                .andWhereIfNotNull(filter.parentDepartmentId) { Departments.parentDepartmentId eq it.value }
                .map(::toDepartment)

        }
    }

    override fun deleteById(id: DepartmentId): Int {
        return transaction {
            Departments.deleteWhere { Departments.id eq id.value }
        }
    }

}
