package com.kamal.sahl_notes.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kamal.sahl_notes.models.LoginRequest
import com.kamal.sahl_notes.models.RegisterRequest
import com.kamal.sahl_notes.models.User
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import java.util.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("welcome to sahl sample app")
        }

        post("/user/register") {
            val Register = call.receive<RegisterRequest>()
            val userId = UUID.randomUUID().toString()
            val user = User(userId,Register.name,Register.email,Register.mobile, generateToken(application, Register.email),Register.password)
            val us = register(user)
            call.respond(us)
        }

        post("/user/login") {
            val login = call.receive<LoginRequest>()
            val user  = login(login, generateToken(application, login.email).toString())
            call.respond(user)
        }


        authenticate("auth-jwt") {
            get("/notes") {
                val principal = call.principal<JWTPrincipal>()
                call.respond(getNotes(principal?.payload?.getClaim("email")?.asString().toString()))
            }

            post("/notes") {
                val text = call.receive<String>()
                val principal = call.principal<JWTPrincipal>()
                call.respond(postNote(principal?.payload?.getClaim("email")?.asString().toString(),text ))
            }

        }


    }
}

fun generateToken(application: Application, email: String): String? {
    return JWT.create()
        .withAudience(application.environment.config.property("jwt.audience").getString())
        .withIssuer(application.environment.config.property("jwt.issuer").getString())
        .withClaim("email", email)
        .withExpiresAt(Date(System.currentTimeMillis() + 60000000))
        .sign(Algorithm.HMAC256(application.environment.config.property("jwt.secret").getString()))
}
