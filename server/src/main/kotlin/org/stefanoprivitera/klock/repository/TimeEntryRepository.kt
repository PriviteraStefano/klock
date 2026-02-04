package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.TimeEntry
import org.stefanoprivitera.klock.domain.TimeEntryRequest

interface TimeEntryRepository {
    fun create(entry: TimeEntryRequest.Create): String
    fun findById(id: String): TimeEntry?
    fun findAll(filter: TimeEntryRequest.Filter): List<TimeEntry>
    fun update(department: TimeEntryRequest.Update): Int
    fun deleteById(id: String): Int
}