package org.stefanoprivitera.klock.repository.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import org.stefanoprivitera.klock.domain.User
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.persistance.Users
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun toUser(r: ResultRow): User {
    return User(
        id = UserId(r[Users.id].value),
        email = r[Users.email],
        firstname = r[Users.firstname],
        lastname = r[Users.lastname],
        createdAt = r[Users.createdAt],
        updatedAt = r[Users.updatedAt]
    )
}