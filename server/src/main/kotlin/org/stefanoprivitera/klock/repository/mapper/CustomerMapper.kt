package org.stefanoprivitera.klock.repository.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import org.stefanoprivitera.klock.domain.Customer
import org.stefanoprivitera.klock.domain.CustomerId
import org.stefanoprivitera.klock.persistance.Customers
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun toCustomer(r: ResultRow): Customer {
    return Customer(
        id = CustomerId(r[Customers.id].value),
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