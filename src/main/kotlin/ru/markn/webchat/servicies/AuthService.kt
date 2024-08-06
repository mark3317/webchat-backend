package ru.markn.webchat.servicies

import ru.markn.webchat.dtos.SingInRequest
import ru.markn.webchat.dtos.SingUpRequest

interface AuthService {
    fun createAuthToken(singInRequest: SingInRequest): String
    fun createNewUser(userDto: SingUpRequest): String
}