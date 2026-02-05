package org.stefanoprivitera.klock.service

import org.stefanoprivitera.klock.domain.TimeEntry
import org.stefanoprivitera.klock.domain.TimeEntryId
import org.stefanoprivitera.klock.domain.TimeEntryRequest

interface TimeEntryService {
    fun create(entry: TimeEntryRequest.Create): TimeEntryId
    fun findById(id: TimeEntryId): TimeEntry?
    fun findAll(filter: TimeEntryRequest.Filter): List<TimeEntry>
    fun update(entry: TimeEntryRequest.Update): Int
    fun deleteById(id: TimeEntryId): Int
}