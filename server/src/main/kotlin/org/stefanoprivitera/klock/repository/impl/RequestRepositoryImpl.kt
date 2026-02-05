package org.stefanoprivitera.klock.repository.impl

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Request
import org.stefanoprivitera.klock.domain.RequestId
import org.stefanoprivitera.klock.domain.RequestRequest
import org.stefanoprivitera.klock.persistance.Requests
import org.stefanoprivitera.klock.repository.RequestRepository
import org.stefanoprivitera.klock.repository.mapper.toRequest
import org.stefanoprivitera.klock.repository.utils.andWhereIfNotNull
import kotlin.let
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi

@Single
@OptIn(ExperimentalUuidApi::class)
class RequestRepositoryImpl : RequestRepository {
    override fun create(request: RequestRequest.Create): RequestId {
        return transaction {
            Requests.insertAndGetId {
                it[projectId] = request.projectId.value
                it[contractId] = request.contractId.value
                it[requestType] = request.requestType
                it[details] = request.details
                it[status] = request.status
                it[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.let { RequestId(it.value) }
        }
    }

    override fun update(request: RequestRequest.Update): Int {
        return transaction {
            Requests.update({ Requests.id eq request.id.value }) {
                request.projectId?.let { p -> it[projectId] = p.value }
                request.contractId?.let { c -> it[contractId] = c.value }
                request.requestType?.let { t -> it[requestType] = t }
                request.details?.let { d -> it[details] = d }
                request.status?.let { s -> it[status] = s }
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }
        }
    }

    override fun findById(id: RequestId): Request? {
        return transaction {
            Requests.selectAll()
                .where { Requests.id eq id.value }
                .map(::toRequest)
                .firstOrNull()
        }
    }

    override fun findAll(filter: RequestRequest.Filter): List<Request> {
        return transaction {
            Requests.selectAll()
                .andWhereIfNotNull(filter.projectId) { Requests.projectId eq it.value }
                .andWhereIfNotNull(filter.contractId) { Requests.contractId eq it.value }
                .andWhereIfNotNull(filter.requestType) { Requests.requestType eq it }
                .andWhereIfNotNull(filter.status) { Requests.status eq it }
                .map(::toRequest)
        }
    }

    override fun deleteById(id: RequestId): Int {
        return transaction {
            Requests.deleteWhere { Requests.id eq id.value }
        }
    }

}
