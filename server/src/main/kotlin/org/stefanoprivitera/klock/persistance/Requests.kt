package org.stefanoprivitera.klock.persistance

import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object Requests : UuidTable("requests") {
    val projectId = reference("project_id", Projects)
    val contractId = reference("contract_id", Contracts)
    val requestType = varchar("request_type", 100)
    val details = text("details")
    val status = varchar("status", 50)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
