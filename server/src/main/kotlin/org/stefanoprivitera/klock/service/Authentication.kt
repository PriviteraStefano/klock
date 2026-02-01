package org.stefanoprivitera.klock.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import org.stefanoprivitera.klock.configuration.JWTConfig
import java.util.Date

fun Routing.authentication() {
    val config by lazy { JWTConfig(this.environment.config) }

    post("/logout") {
        call.respondText("Logout successful")
    }
    post("/login") {
        val token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(config.secret))

        call.respondText(token)
    }
}