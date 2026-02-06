package org.stefanoprivitera.klock.domain.request

import kotlinx.serialization.Serializable
import org.stefanoprivitera.klock.domain.UserId

sealed interface UserRequest {
    @Serializable
    data class Create(
        val email: String,
        val firstname: String,
        val lastname: String,
        val password: String
    ) : UserRequest

    @Serializable
    data class Update(
        val id: UserId,
        val firstname: String?,
        val lastname: String?,
        val password: String?
    ) : UserRequest

    @Serializable
    data class Filter(
        val email: String?,
        val firstname: String?,
        val lastname: String?,
    ) : UserRequest
}
