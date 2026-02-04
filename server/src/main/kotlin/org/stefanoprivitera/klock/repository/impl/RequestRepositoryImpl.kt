package org.stefanoprivitera.klock.repository.impl

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Request
import org.stefanoprivitera.klock.domain.RequestRequest
import org.stefanoprivitera.klock.persistance.Requests
import org.stefanoprivitera.klock.repository.RequestRepository
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
@OptIn(ExperimentalUuidApi::class)
class RequestRepositoryImpl : RequestRepository {
    override fun create(request: RequestRequest.Create): Uuid {
        return transaction {
            Requests.insertAndGetId {
                it[projectId] = request.projectId
                it[contractId] = request.contractId
                it[requestType] = request.requestType
                it[details] = request.details
                it[status] = request.status
                it[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.value
        }
    }

    override fun update(request: RequestRequest.Update): Int {
        return transaction {
            val uuid = Uuid.parse(request.id.toString())
            Requests.update({ Requests.id eq uuid }) {
                request.projectId?.let { p -> it[projectId] = p }
                request.contractId?.let { c -> it[contractId] = c }
                request.requestType?.let { t -> it[requestType] = t }
                request.details?.let { d -> it[details] = d }
                request.status?.let { s -> it[status] = s }
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }
        }
    }

    override fun findById(id: String): Request? {
        return transaction {
            val uuid = Uuid.parse(id)
            Requests.selectAll()
                .where { Requests.id eq uuid }
                .map(::toRequest)
                .firstOrNull()
        }
    }

    override fun findAll(filter: RequestRequest.Filter): List<Request> {
        return transaction {
            var query = Requests.selectAll()

            filter.projectId?.let {
                query = query.where { Requests.projectId eq it }
            }
            filter.contractId?.let {
                query = query.where { Requests.contractId eq it }
            }
            filter.requestType?.let {
                query = query.where { Requests.requestType eq it }
            }
            filter.status?.let {
                query = query.where { Requests.status eq it }
            }

            query.map(::toRequest)
        }
    }

    override fun deleteById(id: String): Int {
        return transaction {
            val uuid = Uuid.parse(id)
            Requests.deleteWhere { Requests.id eq uuid }
        }
    }

    private fun toRequest(r: ResultRow): Request {
        return Request(
            id = r[Requests.id].value,
            projectId = r[Requests.projectId].value,
            contractId = r[Requests.contractId].value,
            requestType = r[Requests.requestType],
            details = r[Requests.details],
            status = r[Requests.status],
            createdAt = r[Requests.createdAt].toString(),
            updatedAt = r[Requests.updatedAt].toString()
        )
    }
}
