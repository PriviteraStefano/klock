package org.stefanoprivitera.klock.repository.impl

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.User
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.domain.request.UserRequest
import org.stefanoprivitera.klock.persistance.Users
import org.stefanoprivitera.klock.repository.UserRepository
import org.stefanoprivitera.klock.repository.mapper.toUser
import org.stefanoprivitera.klock.repository.utils.andWhereIfNotNull
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi

@Single
@OptIn(ExperimentalUuidApi::class)
class UsersRepositoryImpl : UserRepository {
    override fun create(user: UserRequest.Create): UserId {
        return transaction {
            UserId(Users.insertAndGetId {
                it[email] = user.email
                it[firstname] = user.firstname
                it[lastname] = user.lastname
                it[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.value)
        }
    }

    override fun update(user: UserRequest.Update): Int {
        return transaction {
            Users.update({ Users.id eq user.id.value }) {
                user.firstname?.let { s -> it[firstname] = s }
                user.lastname?.let { s -> it[lastname] = s }
                user.password?.let { s -> it[password] = s }
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }
        }
    }

    override fun findById(id: UserId): User? {
        return transaction {
            Users.selectAll()
                .where { Users.id eq id.value }
                .map(::toUser)
                .firstOrNull()
                ?: return@transaction null
        }
    }

    override fun findByEmail(email: String): User? {
        return transaction {
            Users.selectAll()
                .where { Users.email eq email }
                .map(::toUser)
                .firstOrNull()
                ?: return@transaction null
        }
    }

    override fun findAll(filter: UserRequest.Filter): List<User> {
        return transaction {
            Users.selectAll()
                .andWhereIfNotNull(filter.firstname) { Users.firstname like "%${filter.firstname}%" }
                .andWhereIfNotNull(filter.lastname) { Users.lastname like "%${filter.lastname}%" }
                .andWhereIfNotNull(filter.email) { Users.email like "%${filter.email}%" }
                .map(::toUser)
        }
    }

    override fun deleteById(id: UserId): Int {
        return transaction {
            Users.deleteWhere { Users.id eq id.value }
        }
    }

}