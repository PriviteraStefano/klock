package org.stefanoprivitera.klock.configuration

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.response.respond
import org.koin.core.annotation.Singleton

fun Application.jwtAuth() {
    val config by lazy { JWTConfig(environment.config) }

    authentication {
        jwt {
            realm = config.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.audience)) JWTPrincipal(credential.payload) else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}

@Singleton
class JWTConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String
) {
    constructor(applicationConfig: ApplicationConfig) : this(
        secret = applicationConfig.property("jwt.secret").getString(),
        issuer = applicationConfig.property("jwt.issuer").getString(),
        audience = applicationConfig.property("jwt.audience").getString(),
        realm = applicationConfig.property("jwt.realm").getString()
    )
}