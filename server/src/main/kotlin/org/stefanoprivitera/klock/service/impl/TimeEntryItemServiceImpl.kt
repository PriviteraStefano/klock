package org.stefanoprivitera.klock.service.impl

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.TimeEntryId
import org.stefanoprivitera.klock.domain.TimeEntryItem
import org.stefanoprivitera.klock.domain.TimeEntryItemId
import org.stefanoprivitera.klock.domain.request.TimeEntryItemRequest
import org.stefanoprivitera.klock.repository.TimeEntryItemRepository
import org.stefanoprivitera.klock.service.TimeEntryItemService

@Single
class TimeEntryItemServiceImpl(
    private val timeEntryItemRepository: TimeEntryItemRepository
) : TimeEntryItemService {
    override fun create(item: TimeEntryItemRequest.Create): TimeEntryItemId {
        return timeEntryItemRepository.create(item)
    }

    override fun update(item: TimeEntryItemRequest.Update): Int {
        return timeEntryItemRepository.update(item)
    }

    override fun findById(id: TimeEntryItemId): TimeEntryItem? {
        return timeEntryItemRepository.findById(id)
    }

    override fun findAll(filter: TimeEntryItemRequest.Filter): List<TimeEntryItem> {
        return timeEntryItemRepository.findAll(filter)
    }

    override fun deleteById(id: TimeEntryItemId): Int {
        return timeEntryItemRepository.deleteById(id)
    }

    override fun deleteByTimeEntryId(timeEntryId: TimeEntryId): Int {
        return timeEntryItemRepository.deleteByTimeEntryId(timeEntryId)
    }
}
