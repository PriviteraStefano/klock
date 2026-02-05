package org.stefanoprivitera.klock.service

import org.stefanoprivitera.klock.domain.Contract
import org.stefanoprivitera.klock.domain.ContractId
import org.stefanoprivitera.klock.domain.ContractRequest

interface ContractService {
    fun create(contract: ContractRequest.Create): ContractId
    fun findAll(filter: ContractRequest.Filter): List<Contract>
    fun findById(id: ContractId): Contract?
    fun update(contract: ContractRequest.Update): Int
    fun deleteById(id: ContractId): Int
}