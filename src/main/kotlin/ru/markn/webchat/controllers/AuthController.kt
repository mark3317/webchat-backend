package ru.markn.webchat.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.*
import ru.markn.webchat.dtos.SingInRequest
import ru.markn.webchat.dtos.UserSaveDto
import ru.markn.webchat.dtos.UserDto
import ru.markn.webchat.dtos.UserUpdateDto
import ru.markn.webchat.servicies.AuthService
import ru.markn.webchat.servicies.BlackJwtService
import ru.markn.webchat.servicies.UserService
import ru.markn.webchat.utils.toDto
import java.security.Principal

@Tag(
    name = "Auth Controller",
    description = "Controller for authentication, registration and user information"
)
@RestController
class AuthController(
    private val authService: AuthService,
    private val userService: UserService,
    private val blackJwtService: BlackJwtService
) {
    @Operation(
        summary = "Authentication",
        description = "Authenticates a user and returns a JWT token. The user can authenticate by username or email",
        responses = [ApiResponse(responseCode = "200", description = "Returns JWT token")]
    )
    @PostMapping("/signIn")
    fun singIn(@Valid @RequestBody singInRequest: SingInRequest): String = authService.createAuthToken(singInRequest)

    @Operation(
        summary = "Registration new user",
        description = "Registers a new user and returns a JWT token",
        responses = [ApiResponse(responseCode = "200", description = "Returns JWT token")]
    )
    @PostMapping("/signUp")
    fun singUp(@Valid @RequestBody userSaveDto: UserSaveDto): String {
        userService.addUser(userSaveDto)
        return authService.createAuthToken(SingInRequest(userSaveDto.username, userSaveDto.password))
    }

    @Operation(
        summary = "Log out of the system",
        description = "Invalidates the JWT token",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @GetMapping("/signOut")
    fun signOut(
        @Parameter(hidden = true) @NotBlank @RequestHeader("Authorization") authorizationHeader: String,
        principal: Principal
    ) = blackJwtService.addBlackJwt(principal, authorizationHeader)

    @Operation(
        summary = "Get information about authenticated user",
        description = "Returns user information based on JWT token",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @GetMapping("/profile")
    fun getUserData(principal: Principal): UserDto = userService.getUserByUsername(principal.name).toDto()

    @Operation(
        summary = "Update user information",
        description = "Updates user information based on ID",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @PutMapping("/profile")
    fun updateUserData(
        @Valid @RequestBody userUpdate: UserUpdateDto,
        principal: Principal
    ): UserDto {
        val userAuthDto = userService.getUserByUsername(principal.name).toDto()
        return userService.updateUser(
            userAuthDto.copy(
                username = userUpdate.username ?: userAuthDto.username,
                email = userUpdate.email ?: userAuthDto.email,
                password = userUpdate.password ?: userAuthDto.password
            )
        ).toDto()
    }
}