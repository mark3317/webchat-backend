package ru.markn.webchat.servicies

import ru.markn.webchat.dtos.SingInRequest

interface AuthService {
    fun createAuthToken(singInRequest: SingInRequest): String
}