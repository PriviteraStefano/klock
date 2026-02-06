package org.stefanoprivitera.klock.service.impl

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.WorkGroup
import org.stefanoprivitera.klock.domain.WorkGroupId
import org.stefanoprivitera.klock.domain.request.WorkGroupRequest
import org.stefanoprivitera.klock.repository.WorkGroupRepository
import org.stefanoprivitera.klock.service.WorkGroupService

@Single
class WorkGroupServiceImpl(
    private val workGroupRepository: WorkGroupRepository
) : WorkGroupService {
    override fun create(workGroup: WorkGroupRequest.Create): WorkGroupId {
        return workGroupRepository.create(workGroup)
    }

    override fun findById(id: WorkGroupId): WorkGroup? {
        return workGroupRepository.findById(id)
    }

    override fun findAll(filter: WorkGroupRequest.Filter): List<WorkGroup> {
        return workGroupRepository.findAll(filter)
    }

    override fun update(workGroup: WorkGroupRequest.Update): Int {
        return workGroupRepository.update(workGroup)
    }

    override fun deleteById(id: WorkGroupId): Int {
        return workGroupRepository.deleteById(id)
    }
}
