package org.stefanoprivitera.klock.service.impl

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.UserId
import org.stefanoprivitera.klock.domain.WorkGroupId
import org.stefanoprivitera.klock.domain.WorkGroupUser
import org.stefanoprivitera.klock.domain.WorkGroupUserId
import org.stefanoprivitera.klock.domain.request.WorkGroupUserRequest
import org.stefanoprivitera.klock.repository.WorkGroupUserRepository
import org.stefanoprivitera.klock.service.WorkGroupUserService

@Single
class WorkGroupUserServiceImpl(
    private val workGroupUserRepository: WorkGroupUserRepository
) : WorkGroupUserService {
    override fun create(workGroupUser: WorkGroupUserRequest.Create): WorkGroupUserId {
        return workGroupUserRepository.create(workGroupUser)
    }

    override fun findAll(filter: WorkGroupUserRequest.Filter): List<WorkGroupUser> {
        return workGroupUserRepository.findAll(filter)
    }

    override fun deleteById(id: WorkGroupUserId): Int {
        return workGroupUserRepository.deleteById(id)
    }

    override fun deleteWorkGroupUser(userId: UserId, workGroupId: WorkGroupId): Int {
        return workGroupUserRepository.deleteWorkGroupUser(userId, workGroupId)
    }
}
