package ru.markn.webchat.utils

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.time.Duration
import java.util.*
import javax.crypto.SecretKey


@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    private val secret: String,
    @Value("\${jwt.lifetime}")
    private var jwtLifetime: Duration
) {
    private val signingKey: Key
        get() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JwtUtil::class.java)
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims: MutableMap<String, Any?> = HashMap()
        val rolesList = userDetails.authorities.map(GrantedAuthority::getAuthority)
        claims["roles"] = rolesList
        val issuedDate = Date()
        val expiredDate = Date(issuedDate.time + jwtLifetime.toMillis())
        return Jwts.builder()
            .claims(claims)
            .subject(userDetails.username)
            .issuedAt(issuedDate)
            .expiration(expiredDate)
            .signWith(signingKey)
            .compact()
    }

    fun getUserName(token: String): String {
        getClaims(token).onSuccess { return it.subject }
        return ""
    }

    fun getRoles(token: String): List<String> {
        getClaims(token).onSuccess {
            it["roles"]?.let { roles ->
                if (roles is List<*>) {
                    return roles.filterIsInstance<String>()
                }
            }
        }
        return emptyList()
    }

    private fun getClaims(token: String): Result<Claims> {
        val exception: Exception
        try {
            return Result.success(
                Jwts.parser()
                    .verifyWith(signingKey as SecretKey)
                    .build()
                    .parseSignedClaims(token)
                    .payload
            )
        } catch (ex: Exception) {
            exception = ex
        }
        LOGGER.warn(exception.message)
        return Result.failure(exception)
    }
}