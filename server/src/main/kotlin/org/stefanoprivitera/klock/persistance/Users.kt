package org.stefanoprivitera.klock.persistance

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object Users : UuidTable("users") {
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 128) // Hashed password
    val firstname = varchar("name", 100)
    val lastname = varchar("surname", 100)
    val createdAt = datetime("created_at").default(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    val updatedAt = datetime("updated_at").default(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
}
