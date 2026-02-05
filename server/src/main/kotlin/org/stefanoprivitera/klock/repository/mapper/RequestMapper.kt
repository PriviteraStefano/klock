package org.stefanoprivitera.klock.repository.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import org.stefanoprivitera.klock.domain.ContractId
import org.stefanoprivitera.klock.domain.ProjectId
import org.stefanoprivitera.klock.domain.Request
import org.stefanoprivitera.klock.domain.RequestId
import org.stefanoprivitera.klock.persistance.Requests
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun toRequest(r: ResultRow): Request {
    return Request(
        id = RequestId(r[Requests.id].value),
        projectId = ProjectId(r[Requests.projectId].value),
        contractId = ContractId(r[Requests.contractId].value),
        requestType = r[Requests.requestType],
        details = r[Requests.details],
        status = r[Requests.status],
        createdAt = r[Requests.createdAt].toString(),
        updatedAt = r[Requests.updatedAt].toString()
    )
}