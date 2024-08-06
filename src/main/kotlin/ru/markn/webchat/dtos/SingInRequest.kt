package ru.markn.webchat.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Request object for user authentication")
data class SingInRequest(
    @Schema(description = "User login, can be username or email", example = "user@example.com")
    @field:NotBlank(message = "Login cannot be empty")
    val login: String,

    @Schema(description = "User password", example = "password123")
    @field:NotBlank(message = "Password cannot be empty")
    val password: String
)
