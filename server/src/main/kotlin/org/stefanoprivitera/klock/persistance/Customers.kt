package org.stefanoprivitera.klock.persistance

import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object Customers : UuidTable("customers") {
    val vatNumber = varchar("vat_number", 50).uniqueIndex()
    val taxCode = varchar("tax_code", 50).uniqueIndex()
    val companyName = varchar("company_name", 255)
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val phone = varchar("phone", 50)
    val address = text("address")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
