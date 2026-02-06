package org.stefanoprivitera.klock.repository.impl

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Customer
import org.stefanoprivitera.klock.domain.CustomerId
import org.stefanoprivitera.klock.domain.request.CustomerRequest
import org.stefanoprivitera.klock.persistance.Customers
import org.stefanoprivitera.klock.repository.CustomerRepository
import org.stefanoprivitera.klock.repository.mapper.toCustomer
import org.stefanoprivitera.klock.repository.utils.andWhereIfNotNull
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi

@Single
@OptIn(ExperimentalUuidApi::class)
class CustomerRepositoryImpl : CustomerRepository {
    override fun create(customer: CustomerRequest.Create): CustomerId {
        return transaction {
            CustomerId(Customers.insertAndGetId {
                it[vatNumber] = customer.vatNumber
                it[taxCode] = customer.taxCode
                it[companyName] = customer.companyName
                it[name] = customer.name
                it[email] = customer.email
                it[phone] = customer.phone
                it[address] = customer.address
                it[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.value)
        }
    }

    override fun update(customer: CustomerRequest.Update): Int {
        return transaction {
            Customers.update({ Customers.id eq customer.id.value }) {
                customer.vatNumber?.let { v -> it[vatNumber] = v }
                customer.taxCode?.let { t -> it[taxCode] = t }
                customer.companyName?.let { c -> it[companyName] = c }
                customer.name?.let { n -> it[name] = n }
                customer.email?.let { e -> it[email] = e }
                customer.phone?.let { p -> it[phone] = p }
                customer.address?.let { a -> it[address] = a }
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }
        }
    }

    override fun findById(id: CustomerId): Customer? {
        return transaction {
            Customers.selectAll()
                .where { Customers.id eq id.value }
                .map(::toCustomer)
                .firstOrNull()
        }
    }

    override fun findAll(filter: CustomerRequest.Filter): List<Customer> {
        return transaction {
            Customers.selectAll()
                .andWhereIfNotNull(filter.name) { Customers.name like "%${filter.name} %" }
                .andWhereIfNotNull(filter.email) { Customers.email like "%${filter.email}%" }
                .andWhereIfNotNull(filter.companyName) { Customers.companyName like "%${filter.companyName}%" }
                .map { toCustomer(it) }
        }
    }

    override fun deleteById(id: CustomerId): Int {
        return transaction {
            Customers.deleteWhere { Customers.id eq id.value }
        }
    }

}
