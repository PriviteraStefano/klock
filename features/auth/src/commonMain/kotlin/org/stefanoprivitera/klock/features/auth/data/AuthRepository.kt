package org.stefanoprivitera.klock.features.auth.data

import org.stefanoprivitera.klock.domain.request.AuthRequest
import org.stefanoprivitera.klock.domain.response.AuthResponse
import org.stefanoprivitera.klock.domain.response.UserResponse

interface AuthRepository {
    suspend fun login(request: AuthRequest.Login): Result<AuthResponse>
    suspend fun register(request: AuthRequest.Register): Result<AuthResponse>
    suspend fun logout(request: AuthRequest.Logout): Result<Unit>
    suspend fun refreshToken(request: AuthRequest.RefreshToken): Result<String>
    suspend fun getCurrentUser(): Result<UserResponse>
}