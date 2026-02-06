package org.stefanoprivitera.klock.domain

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JvmInline
@Serializable
value class CustomerId @OptIn(ExperimentalUuidApi::class) constructor(val value: Uuid)

@OptIn(ExperimentalUuidApi::class)
data class Customer(
    val id: CustomerId,
    val vatNumber: String,
    val taxCode: String,
    val companyName: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val createdAt: String,
    val updatedAt: String
)

