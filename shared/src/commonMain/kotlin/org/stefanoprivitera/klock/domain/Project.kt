package org.stefanoprivitera.klock.domain

import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Project(
    val id: Uuid,
    val name: String,
    val customer: String,
    val managerId: Uuid,
    val departmentId: Uuid,
    val active: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
