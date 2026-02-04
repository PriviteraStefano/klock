package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.Customer
import org.stefanoprivitera.klock.domain.CustomerRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface CustomerRepository {
    fun create(customer: CustomerRequest.Create): Uuid
    fun findById(id: Uuid): Customer?
    fun findAll(filter: CustomerRequest.Filter): List<Customer>
    fun update(customer: CustomerRequest.Update): Int
    fun deleteById(id: Uuid): Int
}