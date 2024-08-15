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
    private val jwtLifetime: Duration
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
            .setClaims(claims)
            .setSubject(userDetails.username)
            .setIssuedAt(issuedDate)
            .setExpiration(expiredDate)
            .signWith(signingKey)
            .compact()
    }

    fun getUsernameFromToken(token: String): String {
        getClaims(token).onSuccess { return it.subject }
        return ""
    }

    fun getRolesFromToken(token: String): List<String> {
        getClaims(token).onSuccess {
            it["roles"]?.let { roles ->
                if (roles is List<*>) {
                    return roles.filterIsInstance<String>()
                }
            }
        }
        return emptyList()
    }

    fun getTokenLifetimeInSecond(token: String): Long {
        getClaims(token).onSuccess {
            val currentDate = Date(System.currentTimeMillis())
            val expiredAt = it.expiration
            if (expiredAt != null) {
                return Duration.between(currentDate.toInstant(), expiredAt.toInstant()).toSeconds()
            }
        }
        return Duration.ZERO.toSeconds()
    }

    private fun getClaims(token: String): Result<Claims> {
        val exception: Exception
        try {
            return Result.success(
                Jwts.parserBuilder()
                    .setSigningKey(signingKey as SecretKey)
                    .build()
                    .parseClaimsJwt(token)
                    .body
            )
        } catch (ex: Exception) {
            exception = ex
        }
        LOGGER.warn(exception.message)
        return Result.failure(exception)
    }
}