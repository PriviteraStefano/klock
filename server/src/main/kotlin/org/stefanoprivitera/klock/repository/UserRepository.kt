package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.User
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.domain.request.UserRequest

interface UserRepository {
    fun create(user: UserRequest.Create): UserId
    fun update(user: UserRequest.Update): Int
    fun findById(id: UserId): User?
    fun findByEmail(email: String): User?
    fun findAll(filter: UserRequest.Filter): List<User>
    fun deleteById(id: UserId): Int
}