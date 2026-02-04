package org.stefanoprivitera.klock.persistance

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object WorkGroupUsers : UuidTable() {
    val workGroupId = reference("workGroupId", WorkGroups, ReferenceOption.CASCADE)
    val userId = reference("userId", Users, ReferenceOption.CASCADE)
}