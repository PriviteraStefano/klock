package org.stefanoprivitera.klock.persistance

import org.jetbrains.exposed.v1.core.dao.id.UuidTable

object WorkGroups: UuidTable() {
    val name = varchar("name", 255).uniqueIndex()
    val description = text("description").nullable()
}