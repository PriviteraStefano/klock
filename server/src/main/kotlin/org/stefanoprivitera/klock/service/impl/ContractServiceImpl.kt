package org.stefanoprivitera.klock.service.impl

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Contract
import org.stefanoprivitera.klock.domain.ContractId
import org.stefanoprivitera.klock.domain.request.ContractRequest
import org.stefanoprivitera.klock.repository.ContractRepository
import org.stefanoprivitera.klock.service.ContractService

@Single
class ContractServiceImpl(
    private val contractRepository: ContractRepository
) : ContractService {
    override fun create(contract: ContractRequest.Create): ContractId {
        return contractRepository.create(contract)
    }

    override fun findAll(filter: ContractRequest.Filter): List<Contract> {
        return contractRepository.findAll(filter)
    }

    override fun findById(id: ContractId): Contract? {
        return contractRepository.findById(id)
    }

    override fun update(contract: ContractRequest.Update): Int {
        return contractRepository.update(contract)
    }

    override fun deleteById(id: ContractId): Int {
        return contractRepository.deleteById(id)
    }
}
