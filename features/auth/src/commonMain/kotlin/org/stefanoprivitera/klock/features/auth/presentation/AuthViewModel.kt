package org.stefanoprivitera.klock.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.stefanoprivitera.klock.features.auth.usecase.AuthUseCase

class AuthViewModel(private val authUseCase: AuthUseCase) : ViewModel(), KoinComponent {
    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state
        .asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), _state.value)

    fun processIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.Login -> handleLogin(intent.email, intent.password)
            is AuthIntent.Register -> handleRegister(
                intent.email,
                intent.password,
                intent.firstName,
                intent.lastName
            )

            AuthIntent.Logout -> handleLogout()
            AuthIntent.DismissError -> dismissError()
        }
    }

    private fun handleLogin(email: String, password: String) {
        viewModelScope.launch {
            authUseCase.login(email, password).fold(
                onSuccess = { token ->
                    authUseCase.getCurrentUser().fold(
                        onSuccess = { user ->
                            _state.update {
                                AuthState.Authenticated(user)
                            }
                        },
                        onFailure = {
                            _state.update {
                                AuthState.Error("Login successful but failed to fetch user data")
                            }
                        }
                    )
                },
                onFailure = { exception ->
                    _state.update {
                        AuthState.Error(exception.message ?: "Login failed")
                    }
                }
            )
        }
    }

    private fun handleRegister(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ) {
        viewModelScope.launch {
            _state.update { AuthState.Registering }
            authUseCase.register(email, password, firstName, lastName).fold(
                onSuccess = { tokens -> // Save tokens
                    authUseCase.getCurrentUser().fold(
                        onSuccess = { user ->
                            _state.update {
                                AuthState.Authenticated(user)
                            }
                        },
                        onFailure = {
                            _state.update {
                                AuthState.Error("Registration successful but login failed")
                            }
                        }
                    )
                },
                onFailure = { exception ->
                    _state.update {
                        AuthState.Error(exception.message ?: "Registration failed")
                    }
                }
            )
        }
    }

    private fun handleLogout() {
        viewModelScope.launch {
            _state.update { AuthState.LoggingOut }
            authUseCase.logout("") //TODO: pass the actual refresh token here
                .fold(
                    onSuccess = {
                        _state.update { AuthState.Unauthenticated }
                    },
                    onFailure = { exception ->
                        _state.update { AuthState.Error(exception.message ?: "Logout failed") }
                    }
                )
        }
    }

    private fun dismissError() {
        if (_state.value is AuthState.Error) {
            _state.update { AuthState.Idle }
        }
    }
}
