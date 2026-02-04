package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.Contract
import org.stefanoprivitera.klock.domain.ContractRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface ContractRepository {
    fun create(contract: ContractRequest.Create): Uuid
    fun findAll(filter: ContractRequest.Filter): List<Contract>
    fun findById(id: Uuid): Contract?
    fun update(contract: ContractRequest.Update): Int
    fun deleteById(id: Uuid): Int
}