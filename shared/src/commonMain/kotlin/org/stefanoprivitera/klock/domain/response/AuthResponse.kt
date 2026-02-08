package org.stefanoprivitera.klock.domain.response

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)
