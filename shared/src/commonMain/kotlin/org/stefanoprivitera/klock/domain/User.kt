package org.stefanoprivitera.klock.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class User(
    val id: Uuid,
    val email: String,
    val firstname: String,
    val lastname: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

sealed class UserRequest {
    @Serializable
    data class Create(
        val email: String,
        val firstname: String,
        val lastname: String,
        val password: String
    ) : UserRequest()

    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Update(
        val id: Uuid,
        val firstname: String?,
        val lastname: String?,
        val password: String?
    ) : UserRequest()
}
