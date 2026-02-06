package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.Request
import org.stefanoprivitera.klock.domain.RequestId
import org.stefanoprivitera.klock.domain.request.RequestRequest

interface RequestRepository {
    fun findAll(filter: RequestRequest.Filter): List<Request>
    fun findById(id: RequestId): Request?
    fun create(request: RequestRequest.Create): RequestId
    fun update(request: RequestRequest.Update): Int
    fun deleteById(id: RequestId): Int
}