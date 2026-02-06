package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.Customer
import org.stefanoprivitera.klock.domain.CustomerId
import org.stefanoprivitera.klock.domain.request.CustomerRequest

interface CustomerRepository {
    fun create(customer: CustomerRequest.Create): CustomerId
    fun findById(id: CustomerId): Customer?
    fun findAll(filter: CustomerRequest.Filter): List<Customer>
    fun update(customer: CustomerRequest.Update): Int
    fun deleteById(id: CustomerId): Int
}