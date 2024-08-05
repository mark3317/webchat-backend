package ru.markn.webchat.servicies

import ru.markn.webchat.dtos.SingInRequest
import ru.markn.webchat.dtos.JwtDto
import ru.markn.webchat.dtos.SingUpRequest

interface AuthService {
    fun createAuthToken(singInRequest: SingInRequest): JwtDto
    fun createNewUser(userDto: SingUpRequest): JwtDto
}