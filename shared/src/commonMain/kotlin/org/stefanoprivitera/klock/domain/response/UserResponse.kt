package org.stefanoprivitera.klock.domain.response

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.User
import org.stefanoprivitera.klock.domain.UserId
import kotlin.uuid.ExperimentalUuidApi



@Serializable
@OptIn(ExperimentalUuidApi::class)
data class UserResponse(
    val id: UserId,
    val email: String,
    val firstname: String,
    val lastname: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(user: User): UserResponse = UserResponse(
            id = user.id,
            email = user.email,
            firstname = user.firstname,
            lastname = user.lastname,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}
