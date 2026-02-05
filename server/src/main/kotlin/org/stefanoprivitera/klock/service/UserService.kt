package org.stefanoprivitera.klock.service

import org.stefanoprivitera.klock.domain.User
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.domain.UserRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface UserService {
    fun create(user: UserRequest.Create): Uuid
    fun update(user: UserRequest.Update): Int
    fun findById(id: UserId): User?
    fun findByEmail(email: String): User?
    fun findAll(filter: UserRequest.Filter): List<User>
    fun deleteById(id: UserId): Int
}