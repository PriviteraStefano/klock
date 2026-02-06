package org.stefanoprivitera.klock.domain

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JvmInline
@Serializable
value class RequestId @OptIn(ExperimentalUuidApi::class) constructor(val value: Uuid)

@OptIn(ExperimentalUuidApi::class)
data class Request(
    val id: RequestId,
    val projectId: ProjectId,
    val contractId: ContractId,
    val requestType: String, // e.g., "Change", "Extension", etc.
    val details: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

