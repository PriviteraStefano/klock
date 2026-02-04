package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.WorkGroup
import org.stefanoprivitera.klock.domain.WorkGroupRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface WorkGroupRepository {
    fun create(workGroup: WorkGroupRequest.Create): Uuid
    fun findById(id: Uuid): WorkGroup?
    fun findAll(filter: WorkGroupRequest.Filter): List<WorkGroup>
    fun update(workGroup: WorkGroupRequest.Update): Int
    fun deleteById(id: Uuid): Int
}