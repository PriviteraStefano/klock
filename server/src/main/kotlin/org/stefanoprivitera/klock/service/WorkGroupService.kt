package org.stefanoprivitera.klock.service

import org.stefanoprivitera.klock.domain.WorkGroup
import org.stefanoprivitera.klock.domain.WorkGroupId
import org.stefanoprivitera.klock.domain.request.WorkGroupRequest

interface WorkGroupService {
    fun create(workGroup: WorkGroupRequest.Create): WorkGroupId
    fun findById(id: WorkGroupId): WorkGroup?
    fun findAll(filter: WorkGroupRequest.Filter): List<WorkGroup>
    fun update(workGroup: WorkGroupRequest.Update): Int
    fun deleteById(id: WorkGroupId): Int
}