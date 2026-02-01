package org.stefanoprivitera.klock.domain

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Department(
    val id: Uuid,
    val name: String,
    val description: String,
    val parentDepartmentId: Uuid?
)
