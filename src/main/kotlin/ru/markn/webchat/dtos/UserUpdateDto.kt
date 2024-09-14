package ru.markn.webchat.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import ru.markn.webchat.utils.NotEmptyOrNull

data class UserUpdateDto(
    @Schema(description = "Username for the new user", example = "newuser")
    @field:NotEmptyOrNull(message = "Username cannot be empty")
    val username: String?,

    @Schema(description = "Password for the new user", example = "password123")
    @field:NotEmptyOrNull(message = "Password cannot be empty")
    val password: String?,

    @Schema(description = "Email for the new user", example = "newuser@example.com")
    @field:NotEmptyOrNull(message = "Email cannot be empty")
    @field:Email(message = "Invalid email format")
    val email: String?
)
