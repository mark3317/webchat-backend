package ru.markn.webchat.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.markn.webchat.dtos.JwtDto
import ru.markn.webchat.dtos.SingInRequest
import ru.markn.webchat.dtos.SingUpRequest
import ru.markn.webchat.models.User
import ru.markn.webchat.servicies.AuthService
import ru.markn.webchat.servicies.UserService
import java.security.Principal

@RestController
class AuthController(
    private val authService: AuthService,
    private val userService: UserService
) {
    @PostMapping("/singIn")
    fun singIn(@RequestBody singInRequest: SingInRequest): JwtDto = authService.createAuthToken(singInRequest)

    @PostMapping("/singUp")
    fun singUp(@RequestBody singUpRequest: SingUpRequest): JwtDto = authService.createNewUser(singUpRequest)

    @GetMapping("/profile")
    fun getUserData(principal: Principal): User = userService.getUserByUsername(principal.name)
}