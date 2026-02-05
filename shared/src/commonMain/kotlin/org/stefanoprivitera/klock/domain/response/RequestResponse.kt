package org.stefanoprivitera.klock.domain.response

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.*
import kotlin.uuid.ExperimentalUuidApi

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class RequestResponse(
    val id: RequestId,
    val projectId: ProjectId,
    val contractId: ContractId,
    val requestType: String,
    val details: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun from(request: Request): RequestResponse = RequestResponse(
            id = request.id,
            projectId = request.projectId,
            contractId = request.contractId,
            requestType = request.requestType,
            details = request.details,
            status = request.status,
            createdAt = request.createdAt,
            updatedAt = request.updatedAt
        )
    }
}
