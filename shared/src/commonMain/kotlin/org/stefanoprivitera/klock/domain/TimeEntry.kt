package org.stefanoprivitera.klock.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TimeEntry(
    val id: Uuid,
    val userId: Uuid,
    val date: LocalDate,
    val type: String, // "work", "permit", "holiday", "learning", "demo"
    val totalHours: Double,
    val status: String,
    val metadata: TimeEntryMetadata,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

@OptIn(ExperimentalUuidApi::class)
sealed class TimeEntryMetadata {
    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    @SerialName("Work")
    data class Work(
        val projectId: Uuid,
        val description: String,
        val location: String
    ) : TimeEntryMetadata()

    @Serializable
    @SerialName("Holiday")
    data object Holiday : TimeEntryMetadata()

    @Serializable
    @SerialName("Learning")
    data class Learning(val topic: String, val description: String, val isStudent: Boolean) : TimeEntryMetadata()

    @Serializable
    @SerialName("Demo")
    data class Demo(
        val customerId: String,
        val managerId: String,
        val projectId: Uuid
    ) : TimeEntryMetadata()

    @Serializable
    @SerialName("Permit")
    data class Permit(val type: String) : TimeEntryMetadata()
}