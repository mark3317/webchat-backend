package ru.markn.webchat.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import kotlin.test.Test
import kotlin.time.Duration


@SpringBootTest
class JwtUtilTest{
    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Value("\${jwt.secret}")
    private  var secret: String = ""

    @Value("\${jwt.lifetime}")
    private  var jwtLifetime: Duration = Duration.ZERO

    @Test
    fun `generateToken should create a valid token`() {
        val userDetails = User("testUser", "password", listOf(SimpleGrantedAuthority("ROLE_USER")))
        val token = jwtUtil.generateToken(userDetails)

        assertNotNull(token)
        assertTrue(token.isNotEmpty())

        // Проверка что токен действительно содержит данные
        val claims = Jwts.parserBuilder().setSigningKey(secret.toByteArray()).build().parseClaimsJws(token).body
        assertEquals("testUser", claims.subject)
        assertEquals(listOf("ROLE_USER"), claims["roles"])
    }

    @Test
    fun `token should expire according to jwtLifetime`() {
        val userDetails = User("testUser", "password", listOf(SimpleGrantedAuthority("ROLE_USER")))
        val token = jwtUtil.generateToken(userDetails)

        val claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secret.toByteArray()))
            .build()
            .parseClaimsJws(token)
            .body

        val issuedAt = claims.issuedAt
        val expiration = claims.expiration

        val jwtLifetimeMillis = jwtLifetime.inWholeSeconds * 1000 + jwtLifetime.inWholeNanoseconds / 1000000
        // Проверка времени жизни токена
        assertEquals(jwtLifetimeMillis, expiration.time - issuedAt.time)
    }

    @Test
    fun `getUserName should extract correct username from token`() {
        val userDetails = User("testUser", "password", listOf(SimpleGrantedAuthority("ROLE_USER")))
        val token = jwtUtil.generateToken(userDetails)

        val username = jwtUtil.getUserName(token)
        assertEquals("testUser", username)
    }

    @Test
    fun `getRoles should extract correct roles from token`() {
        val userDetails = User("testUser", "password", listOf(SimpleGrantedAuthority("ROLE_USER"), SimpleGrantedAuthority("ROLE_ADMIN")))
        val token = jwtUtil.generateToken(userDetails)

        val roles = jwtUtil.getRoles(token)
        assertEquals(2, roles.size)
        assertTrue(roles.contains("ROLE_USER"))
        assertTrue(roles.contains("ROLE_ADMIN"))
    }

    @Test
    fun `getUserName should return empty string for invalid token`() {
        val invalidToken = "invalid.token.here"

        val username = jwtUtil.getUserName(invalidToken)
        assertEquals("", username)
    }

    @Test
    fun `getRoles should return empty list for invalid token`() {
        val invalidToken = "invalid.token.here"

        val roles = jwtUtil.getRoles(invalidToken)
        assertTrue(roles.isEmpty())
    }
}