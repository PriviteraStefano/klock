package org.stefanoprivitera.klock.features.auth.presentation


sealed class AuthIntent {
    data class Login(val email: String, val password: String) : AuthIntent()
    data class Register(
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String
    ) : AuthIntent()
    object Logout : AuthIntent()
    object DismissError : AuthIntent()
}