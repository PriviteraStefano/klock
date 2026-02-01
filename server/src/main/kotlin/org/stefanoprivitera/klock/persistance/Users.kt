package org.stefanoprivitera.klock.persistance

import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object Users : UuidTable("users") {
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 128) // Hashed password
    val firstname = varchar("name", 100)
    val lastname = varchar("surname", 100)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
