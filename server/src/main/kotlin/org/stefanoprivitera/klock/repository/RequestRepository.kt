package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.Request
import org.stefanoprivitera.klock.domain.RequestRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface RequestRepository {
    fun findAll(filter: RequestRequest.Filter): List<Request>
    fun findById(id: String): Request?
    fun create(request: RequestRequest.Create): Uuid
    fun update(request: RequestRequest.Update): Int
    fun deleteById(id: String): Int
}