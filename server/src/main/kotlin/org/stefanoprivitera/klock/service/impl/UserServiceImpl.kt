package org.stefanoprivitera.klock.service.impl

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.User
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.domain.UserRequest
import org.stefanoprivitera.klock.repository.UserRepository
import org.stefanoprivitera.klock.service.UserService
import kotlin.uuid.ExperimentalUuidApi

@Single
@OptIn(ExperimentalUuidApi::class)
class UserServiceImpl(
    val userRepository: UserRepository
) : UserService {
    override fun create(user: UserRequest.Create): UserId {
        return userRepository.create(user)
    }

    override fun update(user: UserRequest.Update): Int {
        return userRepository.update(user)
    }

    override fun findById(id: UserId): User? {
        return userRepository.findById(id)
    }

    override fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    override fun findAll(filter: UserRequest.Filter): List<User> {
        return userRepository.findAll(filter)
    }

    override fun deleteById(id: UserId): Int {
        return userRepository.deleteById(id)
    }
}