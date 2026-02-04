package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.Request
import org.stefanoprivitera.klock.domain.RequestRequest

interface RequestRepository {
    fun findAll(filter: RequestRequest.Filter): List<Request>
    fun findById(id: String): Request?
    fun create(request: RequestRequest.Create): String
    fun update(request: RequestRequest.Update): Int
    fun deleteById(id: String): Int
}