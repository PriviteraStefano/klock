package org.stefanoprivitera.klock.service.impl

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Request
import org.stefanoprivitera.klock.domain.RequestId
import org.stefanoprivitera.klock.domain.request.RequestRequest
import org.stefanoprivitera.klock.repository.RequestRepository
import org.stefanoprivitera.klock.service.RequestService

@Single
class RequestServiceImpl(
    private val requestRepository: RequestRepository
) : RequestService {
    override fun findAll(filter: RequestRequest.Filter): List<Request> {
        return requestRepository.findAll(filter)
    }

    override fun findById(id: RequestId): Request? {
        return requestRepository.findById(id)
    }

    override fun create(request: RequestRequest.Create): RequestId {
        return requestRepository.create(request)
    }

    override fun update(request: RequestRequest.Update): Int {
        return requestRepository.update(request)
    }

    override fun deleteById(id: RequestId): Int {
        return requestRepository.deleteById(id)
    }
}
