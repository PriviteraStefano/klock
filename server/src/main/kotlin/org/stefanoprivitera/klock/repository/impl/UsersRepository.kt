package org.stefanoprivitera.klock.repository.impl

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.User
import org.stefanoprivitera.klock.domain.UserRequest
import org.stefanoprivitera.klock.persistance.Users
import org.stefanoprivitera.klock.persistance.Users.createdAt
import org.stefanoprivitera.klock.persistance.Users.firstname
import org.stefanoprivitera.klock.persistance.Users.lastname
import org.stefanoprivitera.klock.persistance.Users.updatedAt
import org.stefanoprivitera.klock.repository.IUserRepository
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
@OptIn(ExperimentalUuidApi::class)
class UsersRepository : IUserRepository {
    override fun create(user: UserRequest.Create): Uuid {
        return transaction {
            Users.insertAndGetId {
                it[email] = user.email
                it[firstname] = user.firstname
                it[lastname] = user.lastname
                it[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.value
        }
    }

    override fun update(user: UserRequest.Update): Int {
        return transaction {
            Users.update({ Users.id eq user.id }) {
                user.firstname?.let { s -> it[firstname] = s }
                user.lastname?.let { s -> it[lastname] = s }
                user.password?.let { s -> it[password] = s }
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }
        }
    }

    override fun findById(id: Uuid): User? {
        return transaction {
            Users.selectAll()
                .where { Users.id eq id }
                .firstOrNull()
                ?.toUser()
                ?: return@transaction null
        }
    }

    override fun findByEmail(email: String): User? {
        return transaction {
            Users.selectAll()
                .where { Users.email eq email }
                .firstOrNull()
                ?.toUser()
                ?: return@transaction null
        }
    }

    override fun findAll(): List<User> {
        return transaction {
            Users.selectAll()
                .map { it.toUser() }
        }
    }

    override fun deleteById(id: Uuid): Int {
        return transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }

    private fun ResultRow.toUser(): User {
        return User(
            id = this[Users.id].value,
            email = this[Users.email],
            firstname = this[firstname],
            lastname = this[lastname],
            createdAt = this[createdAt],
            updatedAt = this[updatedAt]
        )
    }
}