package com.hadit1993.eshopback.security.jwt
import io.jsonwebtoken.Claims
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

enum class JwtTokenType(val type: String, val expiration: Date) {
    Authentication("authentication", Date.from(Instant.now().plus(90, ChronoUnit.DAYS))),
    AccountActivation("account-activation", Date.from(Instant.now().plus(15, ChronoUnit.MINUTES))),
    PasswordRecovery("password-recovery", Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))

}

interface JwtService {
    fun generateToken(username: String, authorities: List<String>, tokenType: JwtTokenType): String
    fun verify(token: String, tokenType: JwtTokenType): Claims
}