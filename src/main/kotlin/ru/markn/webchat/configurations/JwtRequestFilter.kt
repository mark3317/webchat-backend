package ru.markn.webchat.configurations

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.markn.webchat.servicies.BlackJwtService
import ru.markn.webchat.utils.JwtUtil

@Component
class JwtRequestFilter(
    private val jwtUtil: JwtUtil,
    private val blackJwtService: BlackJwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        authHeader?.let {
            if (it.startsWith("Bearer ")) {
                val token = it.replace("Bearer ", "")
                if (!blackJwtService.isBlackJwt(token)) {
                    val username = jwtUtil.getUsernameFromToken(token)
                    if (username.isNotEmpty() && SecurityContextHolder.getContext().authentication == null) {
                        val authToken = UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            jwtUtil.getRolesFromToken(token).map { role: String -> SimpleGrantedAuthority(role) }
                        )
                        SecurityContextHolder.getContext().authentication = authToken
                    }
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}