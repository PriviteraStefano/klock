package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.domain.WorkGroupId
import org.stefanoprivitera.klock.domain.WorkGroupUser
import org.stefanoprivitera.klock.domain.WorkGroupUserId
import org.stefanoprivitera.klock.domain.WorkGroupUserRequest

interface WorkGroupUserRepository {
    fun create(workGroupUser: WorkGroupUserRequest.Create): WorkGroupUserId
    fun findAll(filter: WorkGroupUserRequest.Filter): List<WorkGroupUser>
    fun deleteById(id: WorkGroupUserId): Int
    fun deleteWorkGroupUser(userId: UserId, workGroupId: WorkGroupId): Int
}
