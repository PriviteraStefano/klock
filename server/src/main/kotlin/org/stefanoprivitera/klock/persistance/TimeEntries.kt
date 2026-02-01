package org.stefanoprivitera.klock.persistance

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.datetime
import org.jetbrains.exposed.v1.json.jsonb
import org.stefanoprivitera.klock.domain.TimeEntryMetadata
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object TimeEntries : UuidTable("time_entries") {
    val userId = reference("user_id", Users)
    val date = date("date")
    val type = varchar("type", 50) // "work", "permit", "holiday", "learning", "demo"
    val totalHours = decimal("total_hours", 5, 2)
    val status = varchar("status", 50)
    val metadata = jsonb<TimeEntryMetadata>("metadata", Json.Default) // Flexible field for type-specific data
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}