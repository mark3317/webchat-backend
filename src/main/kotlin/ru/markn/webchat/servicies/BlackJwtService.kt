package ru.markn.webchat.servicies

import java.security.Principal

interface BlackJwtService {
    fun addBlackJwt(principal: Principal, authHeader: String)
    fun existBlackJwt(token: String): Boolean
}