package org.stefanoprivitera.klock.repository.impl

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Contract
import org.stefanoprivitera.klock.domain.ContractId
import org.stefanoprivitera.klock.domain.ContractRequest
import org.stefanoprivitera.klock.persistance.Contracts
import org.stefanoprivitera.klock.repository.ContractRepository
import org.stefanoprivitera.klock.repository.mapper.toContract
import org.stefanoprivitera.klock.repository.utils.andWhereIfNotNull
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi

@Single
@OptIn(ExperimentalUuidApi::class)
class ContractRepositoryImpl : ContractRepository {
    override fun create(contract: ContractRequest.Create): ContractId {
        return transaction {
            ContractId(Contracts.insertAndGetId {
                it[customerId] = contract.customerId.value
                it[billingDate] = contract.billingDate
                it[amount] = contract.amount
                it[currency] = contract.currency
                it[status] = contract.status
                it[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.value)
        }
    }

    override fun update(contract: ContractRequest.Update): Int {
        return transaction {
            Contracts.update({ Contracts.id eq contract.id.value }) {
                contract.customerId?.let { c -> it[customerId] = c.value }
                contract.billingDate?.let { b -> it[billingDate] = b }
                contract.amount?.let { a -> it[amount] = a }
                contract.currency?.let { c -> it[currency] = c }
                contract.status?.let { s -> it[status] = s }
                it[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }
        }
    }

    override fun findById(id: ContractId): Contract? {
        return transaction {
            Contracts.selectAll()
                .where { Contracts.id eq id.value }
                .map(::toContract)
                .firstOrNull()
        }
    }

    override fun findAll(filter: ContractRequest.Filter): List<Contract> {
        return transaction {
            Contracts.selectAll()
                .andWhereIfNotNull(filter.customerId) { Contracts.customerId eq it.value }
                .andWhereIfNotNull(filter.billingDateFrom) { Contracts.billingDate greater it }
                .andWhereIfNotNull(filter.billingDateTo) { Contracts.billingDate less it }
                .andWhereIfNotNull(filter.status) { Contracts.status eq it }
                .map(::toContract)
        }
    }

    override fun deleteById(id: ContractId): Int {
        return transaction {
            Contracts.deleteWhere { Contracts.id eq id.value }
        }
    }
}
