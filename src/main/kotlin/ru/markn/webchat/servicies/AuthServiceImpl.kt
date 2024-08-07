package ru.markn.webchat.servicies

import jakarta.transaction.Transactional
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import ru.markn.webchat.dtos.SingInRequest
import ru.markn.webchat.dtos.SingUpRequest
import ru.markn.webchat.utils.JwtUtil

@Service
class AuthServiceImpl(
    private val userService: UserService,
    private val jwtUtil: JwtUtil,
    private val authenticationManager: AuthenticationManager
) : AuthService {
    @Transactional
    override fun createAuthToken(singInRequest: SingInRequest): String {
        val username = getUsernameFromLogin(singInRequest.login)
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                username,
                singInRequest.password
            )
        )
        val userDetails = userService.loadUserByUsername(username)
        return jwtUtil.generateToken(userDetails)
    }

    @Transactional
    override fun createNewUser(userDto: SingUpRequest): String {
        userService.addUser(userDto)
        return createAuthToken(
            SingInRequest(
                login = userDto.username,
                password = userDto.password
            )
        )
    }

    private fun getUsernameFromLogin(login: String): String {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return if (login.matches(emailRegex)) userService.getUserByEmail(login).username else login
    }
}