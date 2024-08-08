package ru.markn.webchat.servicies

import jakarta.transaction.Transactional
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import ru.markn.webchat.dtos.SingInRequest
import ru.markn.webchat.utils.JwtUtil

@Service
@Transactional
class AuthServiceImpl(
    private val userService: UserService,
    private val jwtUtil: JwtUtil,
    private val authenticationManager: AuthenticationManager,
) : AuthService {

    override fun createAuthToken(singInRequest: SingInRequest): String {
        val username = if (EmailValidator().isValid(singInRequest.login, null)) {
            userService.getUserByEmail(singInRequest.login).username
        } else {
            singInRequest.login
        }
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                username,
                singInRequest.password
            )
        )
        val userDetails = userService.loadUserByUsername(username)
        return jwtUtil.generateToken(userDetails)
    }
}