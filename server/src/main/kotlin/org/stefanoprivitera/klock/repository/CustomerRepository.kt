package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.CustomerRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface CustomerRepository {
    fun create(customer: CustomerRequest.Create): String
    fun findById(id: Uuid): String?
    fun findAll(filter: CustomerRequest.Filter): List<String>
    fun update(customer: CustomerRequest.Update): Int
    fun deleteById(id: Uuid): Int
}