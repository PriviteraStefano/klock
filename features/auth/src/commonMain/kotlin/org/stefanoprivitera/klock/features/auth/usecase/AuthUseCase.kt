package org.stefanoprivitera.klock.features.auth.usecase

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.request.AuthRequest
import org.stefanoprivitera.klock.domain.response.AuthResponse
import org.stefanoprivitera.klock.domain.response.UserResponse
import org.stefanoprivitera.klock.features.auth.data.AuthRepository

@Single
class AuthUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return runCatching {
            authRepository
                .login(AuthRequest.Login(email, password))
                .getOrThrow()
        }
    }

    suspend fun register(email: String, password: String, firstName: String, lastName: String): Result<AuthResponse> {
        return runCatching {
            authRepository.register(
                AuthRequest.Register(
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                    password = password
                )
            ).getOrThrow()
        }
    }

    suspend fun logout(refreshToken: String): Result<Unit> {
        return runCatching {
            authRepository.logout(
                AuthRequest.Logout(
                    refreshToken = refreshToken
                )
            ).getOrThrow()
        }
    }

    suspend fun refreshToken(refreshToken: String): Result<String> {
        return runCatching {
            authRepository.refreshToken(
                AuthRequest.RefreshToken(refreshToken = refreshToken)
            ).getOrThrow()
        }
    }

    suspend fun getCurrentUser(): Result<UserResponse> {
        return runCatching {
            authRepository.getCurrentUser().getOrThrow()
        }
    }
}