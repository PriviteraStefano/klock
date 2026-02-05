package org.stefanoprivitera.klock.service.impl

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Customer
import org.stefanoprivitera.klock.domain.CustomerId
import org.stefanoprivitera.klock.domain.CustomerRequest
import org.stefanoprivitera.klock.repository.CustomerRepository
import org.stefanoprivitera.klock.service.CustomerService

@Single
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository
) : CustomerService {
    override fun create(customer: CustomerRequest.Create): CustomerId {
        return customerRepository.create(customer)
    }

    override fun findById(id: CustomerId): Customer? {
        return customerRepository.findById(id)
    }

    override fun findAll(filter: CustomerRequest.Filter): List<Customer> {
        return customerRepository.findAll(filter)
    }

    override fun update(customer: CustomerRequest.Update): Int {
        return customerRepository.update(customer)
    }

    override fun deleteById(id: CustomerId): Int {
        return customerRepository.deleteById(id)
    }
}
