package org.stefanoprivitera.klock.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JvmInline
@Serializable
@OptIn(ExperimentalUuidApi::class)
value class UserId(val value: Uuid) {
    constructor(value: String) : this(Uuid.parse(value))
}

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

