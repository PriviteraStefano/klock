package org.stefanoprivitera.klock.features.auth.presentation

import org.stefanoprivitera.klock.domain.response.UserResponse

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Registering : AuthState()
    data class Authenticated(val user: UserResponse) : AuthState()
    data class Error(val message: String, val code: String? = null) : AuthState()
    object Unauthenticated : AuthState()
    object LoggingOut : AuthState()
}
