package ru.markn.webchat.servicies

import jakarta.transaction.Transactional
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.markn.webchat.dtos.SingInRequest
import ru.markn.webchat.dtos.JwtDto
import ru.markn.webchat.dtos.SingUpRequest
import ru.markn.webchat.utils.JwtUtil

@Service
class AuthServiceImpl(
    private val userService: UserService,
    private val jwtUtil: JwtUtil,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
) : AuthService {
    @Transactional
    override fun createAuthToken(singInRequest: SingInRequest): JwtDto {
        singInRequest.login.ifEmpty { throw IllegalArgumentException("Login is empty") }
        singInRequest.password.ifEmpty { throw IllegalArgumentException("Password is empty") }
        val username = getUsernameFromLogin(singInRequest.login)
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                username,
                singInRequest.password
            )
        )
        val userDetails = userService.loadUserByUsername(username)
        val token = jwtUtil.generateToken(userDetails)
        return JwtDto(token)
    }

    @Transactional
    override fun createNewUser(userDto: SingUpRequest): JwtDto {
        userService.addUser(userDto.copy(password = passwordEncoder.encode(userDto.password)))
        val userDetails = userService.loadUserByUsername(userDto.username)
        val token = jwtUtil.generateToken(userDetails)
        return JwtDto(token)
    }

    private fun getUsernameFromLogin(login: String): String {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return if (login.matches(emailRegex)) userService.getUserByEmail(login).username else login
    }
}