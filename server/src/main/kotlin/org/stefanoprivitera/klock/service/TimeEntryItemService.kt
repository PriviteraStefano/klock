package org.stefanoprivitera.klock.service

import org.stefanoprivitera.klock.domain.TimeEntryId
import org.stefanoprivitera.klock.domain.TimeEntryItem
import org.stefanoprivitera.klock.domain.TimeEntryItemId
import org.stefanoprivitera.klock.domain.request.TimeEntryItemRequest

interface TimeEntryItemService {
    fun create(item: TimeEntryItemRequest.Create): TimeEntryItemId
    fun update(item: TimeEntryItemRequest.Update): Int
    fun findById(id: TimeEntryItemId): TimeEntryItem?
    fun findAll(filter: TimeEntryItemRequest.Filter): List<TimeEntryItem>
    fun deleteById(id: TimeEntryItemId): Int
    fun deleteByTimeEntryId(timeEntryId: TimeEntryId): Int
}
