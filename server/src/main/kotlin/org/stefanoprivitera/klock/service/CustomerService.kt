package org.stefanoprivitera.klock.service

import org.stefanoprivitera.klock.domain.Customer
import org.stefanoprivitera.klock.domain.CustomerId
import org.stefanoprivitera.klock.domain.request.CustomerRequest

interface CustomerService {
    fun create(customer: CustomerRequest.Create): CustomerId
    fun findById(id: CustomerId): Customer?
    fun findAll(filter: CustomerRequest.Filter): List<Customer>
    fun update(customer: CustomerRequest.Update): Int
    fun deleteById(id: CustomerId): Int
}