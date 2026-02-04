package org.stefanoprivitera.klock.repository.impl

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Customer
import org.stefanoprivitera.klock.domain.CustomerRequest
import org.stefanoprivitera.klock.persistance.Customers
import org.stefanoprivitera.klock.repository.CustomerRepository
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
@OptIn(ExperimentalUuidApi::class)
class CustomerRepositoryImpl : CustomerRepository {
    override fun create(customer: CustomerRequest.Create): Uuid {
        return transaction {
            Customers.insertAndGetId {
                it[vatNumber] = customer.vatNumber
                it[taxCode] = customer.taxCode
                it[companyName] = customer.companyName
                it[name] = customer.name
                it[email] = customer.email
                it[phone] = customer.phone
                it[address] = customer.address
                it[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.value
        }
    }

    override fun update(customer: CustomerRequest.Update): Int {
        return transaction {
            Customers.update({ Customers.id eq customer.id }) {
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

    override fun findById(id: Uuid): Customer? {
        return transaction {
            Customers.selectAll()
                .where { Customers.id eq id }
                .map(::toCustomer)
                .firstOrNull()
        }
    }

    override fun findAll(filter: CustomerRequest.Filter): List<Customer> {
        return transaction {
            var query = Customers.selectAll()

            filter.companyName?.let {
                query = query.where { Customers.companyName like "%$it%" }
            }
            filter.name?.let {
                query = query.where { Customers.name like "%$it%" }
            }
            filter.email?.let {
                query = query.where { Customers.email eq it }
            }

            query.map(::toCustomer)
        }
    }

    override fun deleteById(id: Uuid): Int {
        return transaction {
            Customers.deleteWhere { Customers.id eq id }
        }
    }

    private fun toCustomer(r: ResultRow): Customer {
        return Customer(
            id = r[Customers.id].value,
            vatNumber = r[Customers.vatNumber],
            taxCode = r[Customers.taxCode],
            companyName = r[Customers.companyName],
            name = r[Customers.name],
            email = r[Customers.email],
            phone = r[Customers.phone],
            address = r[Customers.address],
            createdAt = r[Customers.createdAt].toString(),
            updatedAt = r[Customers.updatedAt].toString()
        )
    }
}
