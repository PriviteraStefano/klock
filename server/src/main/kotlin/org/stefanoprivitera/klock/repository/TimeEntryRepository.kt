package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.TimeEntry
import org.stefanoprivitera.klock.domain.TimeEntryId
import org.stefanoprivitera.klock.domain.request.TimeEntryRequest

interface TimeEntryRepository {
    fun create(entry: TimeEntryRequest.Create): TimeEntryId
    fun findById(id: TimeEntryId): TimeEntry?
    fun findAll(filter: TimeEntryRequest.Filter): List<TimeEntry>
    fun update(entry: TimeEntryRequest.Update): Int
    fun deleteById(id: TimeEntryId): Int
}