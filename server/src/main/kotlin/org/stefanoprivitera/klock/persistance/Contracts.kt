package org.stefanoprivitera.klock.persistance

import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object Contracts : UuidTable("contracts") {
    val customerId = reference("customer_id", Customers)
    val billingDate = date("billing_date")
    val amount = long("amount") // TODO: Use BigDecimal when supported
    val currency = varchar("currency", 10).default("EUR")
    val status = varchar("status", 50)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
