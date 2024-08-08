package ru.markn.webchat.servicies

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import ru.markn.webchat.models.BlackJwt
import ru.markn.webchat.repositories.BlackJwtRepository
import ru.markn.webchat.utils.JwtUtil
import java.security.Principal

@Service
class BlackJwtServiceImpl(
    private val jwtUtil: JwtUtil,
    private val blackJwtRepository: BlackJwtRepository
) : BlackJwtService {

    override fun addBlackJwt(principal: Principal, authHeader: String) {
        val token = authHeader.replace("Bearer ", "").ifBlank {
            throw BadCredentialsException("Token is empty")
        }
        val blackJwt = BlackJwt(
            token = token,
            tokenTtl = jwtUtil.getTokenLifetimeInSecond(token)
        )
        blackJwtRepository.save(blackJwt)
    }

    override fun isBlackJwt(token: String): Boolean = blackJwtRepository.existsById(token)
}