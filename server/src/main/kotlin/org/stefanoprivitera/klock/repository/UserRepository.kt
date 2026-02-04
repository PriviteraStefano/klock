package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.User
import org.stefanoprivitera.klock.domain.UserRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface UserRepository {
    fun create(user: UserRequest.Create): Uuid
    fun update(user: UserRequest.Update): Int
    fun findById(id: Uuid): User?
    fun findByEmail(email: String): User?
    fun findAll(): List<User>
    fun deleteById(id: Uuid): Int
}