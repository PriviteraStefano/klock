package org.stefanoprivitera.klock.persistance

import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object Expenses : UuidTable("expenses") {
    val userId = reference("user_id", Users)
    val date = date("date")
    val category = varchar("category", 255)
    val amount = decimal("amount", 10, 2)
    val currency = varchar("currency", 10).default("EUR")
    val status = varchar("status", 50) // "pending", "approved", "rejected"
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}