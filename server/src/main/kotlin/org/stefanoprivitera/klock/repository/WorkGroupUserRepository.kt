package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.WorkGroupUser
import org.stefanoprivitera.klock.domain.WorkGroupUserRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface WorkGroupUserRepository {
    fun create(workGroupUser: WorkGroupUserRequest.Create): Uuid
    fun findAll(filter: WorkGroupUserRequest.Filter): List<WorkGroupUser>
    fun deleteById(id: Uuid): Int
    fun deleteByUserAndWorkGroup(userId: Uuid, workGroupId: Uuid): Int
}
