package ru.markn.webchat.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.io.Serializable

@Schema(description = "Request object for user registration")
data class SingUpRequest(
    @Schema(description = "Username for the new user", example = "newuser")
    @field:NotEmpty(message = "Username cannot be empty")
    val username: String,

    @Schema(description = "Password for the new user", example = "password123")
    @field:NotBlank(message = "Password cannot be empty")
    val password: String,

    @Schema(description = "Email for the new user", example = "newuser@example.com")
    @field:NotBlank(message = "Email cannot be empty")
    @field:Email(message = "Invalid email format")
    val email: String
) : Serializable