package ru.markn.webchat.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.markn.webchat.dtos.SingInRequest
import ru.markn.webchat.dtos.SingUpRequest
import ru.markn.webchat.models.User
import ru.markn.webchat.servicies.AuthService
import ru.markn.webchat.servicies.UserService
import java.security.Principal

@Tag(
    name = "Auth Controller",
    description = "Controller for authentication, registration and user information"
)
@RestController
class AuthController(
    private val authService: AuthService,
    private val userService: UserService
) {
    @Operation(
        summary = "Authentication",
        description = "Authenticates a user and returns a JWT token",
        responses = [ApiResponse(responseCode = "200", description = "Returns JWT token")]
    )
    @PostMapping("/singIn")
    fun singIn(@Valid @RequestBody singInRequest: SingInRequest): String = authService.createAuthToken(singInRequest)

    @Operation(
        summary = "Registration new user",
        description = "Registers a new user and returns a JWT token",
        responses = [ApiResponse(responseCode = "200", description = "Returns JWT token")]
    )
    @PostMapping("/singUp")
    fun singUp(@Valid @RequestBody singUpRequest: SingUpRequest): String = authService.createNewUser(singUpRequest)

    @Operation(
        summary = "Get information about authenticated user",
        description = "Returns user information based on JWT token"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/profile")
    fun getUserData(principal: Principal): User = userService.getUserByUsername(principal.name)
}