package org.stefanoprivitera.klock.domain

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Request(
    val id: Uuid,
    val projectId: Uuid,
    val contractId: Uuid,
    val requestType: String, // e.g., "Change", "Extension", etc.
    val details: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

sealed interface RequestRequest {
    @OptIn(ExperimentalUuidApi::class)
    data class Create(
        val projectId: Uuid,
        val contractId: Uuid,
        val requestType: String,
        val details: String,
        val status: String
    ) : RequestRequest

    @OptIn(ExperimentalUuidApi::class)
    data class Update(
        val id: Uuid,
        val projectId: Uuid?,
        val contractId: Uuid?,
        val requestType: String?,
        val details: String?,
        val status: String?
    ) : RequestRequest

    @OptIn(ExperimentalUuidApi::class)
    data class Filter(
        val projectId: Uuid?,
        val contractId: Uuid?,
        val requestType: String?,
        val status: String?
    ) : RequestRequest
}
