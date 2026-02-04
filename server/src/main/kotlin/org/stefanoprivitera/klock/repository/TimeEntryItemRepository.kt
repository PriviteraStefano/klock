package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.TimeEntryItem
import org.stefanoprivitera.klock.domain.TimeEntryItemRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface TimeEntryItemRepository {
    fun create(item: TimeEntryItemRequest.Create): Uuid
    fun update(item: TimeEntryItemRequest.Update): Int
    fun findById(id: Uuid): TimeEntryItem?
    fun findAll(filter: TimeEntryItemRequest.Filter): List<TimeEntryItem>
    fun deleteById(id: Uuid): Int
    fun deleteByTimeEntryId(timeEntryId: Uuid): Int
}
