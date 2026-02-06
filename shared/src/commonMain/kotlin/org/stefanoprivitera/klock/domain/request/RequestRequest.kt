package org.stefanoprivitera.klock.domain.request

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.ContractId
import org.stefanoprivitera.klock.domain.ProjectId
import org.stefanoprivitera.klock.domain.RequestId

sealed interface RequestRequest {
    @Serializable
    data class Create(
        val projectId: ProjectId,
        val contractId: ContractId,
        val requestType: String,
        val details: String,
        val status: String
    ) : RequestRequest

    @Serializable
    data class Update(
        val id: RequestId,
        val projectId: ProjectId?,
        val contractId: ContractId?,
        val requestType: String?,
        val details: String?,
        val status: String?
    ) : RequestRequest

    @Serializable
    data class Filter(
        val projectId: ProjectId?,
        val contractId: ContractId?,
        val requestType: String?,
        val status: String?
    ) : RequestRequest
}
