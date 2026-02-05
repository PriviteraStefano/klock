package org.stefanoprivitera.klock.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JvmInline
@Serializable
value class UserId @OptIn(ExperimentalUuidApi::class) constructor(val value: Uuid)

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class User(
    val id: UserId,
    val email: String,
    val firstname: String,
    val lastname: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

sealed interface UserRequest {
    @Serializable
    data class Create(
        val email: String,
        val firstname: String,
        val lastname: String,
        val password: String
    ) : UserRequest

    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Update(
        val id: UserId,
        val firstname: String?,
        val lastname: String?,
        val password: String?
    ) : UserRequest

    @Serializable
    data class Filter(
        val email: String?,
        val firstname: String?,
        val lastname: String?,
        val groupName: String?,
        val departmentName: String?,
    ) : UserRequest
}