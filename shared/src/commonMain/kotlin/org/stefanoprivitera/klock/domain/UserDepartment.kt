package org.stefanoprivitera.klock.domain

import kotlin.uuid.Uuid
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
data class UserDepartment(
    val departmentId: Uuid,
    val userId: Uuid,
    val id: Uuid,
)


