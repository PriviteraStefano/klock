package org.stefanoprivitera.klock.features.auth.data

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.request.AuthRequest
import org.stefanoprivitera.klock.domain.response.AuthResponse
import org.stefanoprivitera.klock.domain.response.UserResponse

@Single
class AuthRepositoryImpl : AuthRepository {

    override suspend fun login(request: AuthRequest.Login): Result<AuthResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun register(request: AuthRequest.Register): Result<AuthResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(request: AuthRequest.Logout): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken(request: AuthRequest.RefreshToken): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUser(): Result<UserResponse> {
        TODO("Not yet implemented")
    }
}