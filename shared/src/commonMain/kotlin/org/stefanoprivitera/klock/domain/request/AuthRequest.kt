package org.stefanoprivitera.klock.domain.request

sealed interface AuthRequest {
    data class Login(val email: String, val password: String) : AuthRequest
    data class Register(val email: String, val firstName: String, val lastName: String, val password: String) : AuthRequest
    data class Logout(val refreshToken: String) : AuthRequest
    data class RefreshToken(val refreshToken: String) : AuthRequest
}