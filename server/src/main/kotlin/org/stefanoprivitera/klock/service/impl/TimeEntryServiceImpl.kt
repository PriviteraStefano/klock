package org.stefanoprivitera.klock.service.impl

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.TimeEntry
import org.stefanoprivitera.klock.domain.TimeEntryId
import org.stefanoprivitera.klock.domain.request.TimeEntryRequest
import org.stefanoprivitera.klock.repository.TimeEntryRepository
import org.stefanoprivitera.klock.service.TimeEntryService

@Single
class TimeEntryServiceImpl(
    private val timeEntryRepository: TimeEntryRepository
) : TimeEntryService {
    override fun create(entry: TimeEntryRequest.Create): TimeEntryId {
        return timeEntryRepository.create(entry)
    }

    override fun findById(id: TimeEntryId): TimeEntry? {
        return timeEntryRepository.findById(id)
    }

    override fun findAll(filter: TimeEntryRequest.Filter): List<TimeEntry> {
        return timeEntryRepository.findAll(filter)
    }

    override fun update(entry: TimeEntryRequest.Update): Int {
        return timeEntryRepository.update(entry)
    }

    override fun deleteById(id: TimeEntryId): Int {
        return timeEntryRepository.deleteById(id)
    }
}
