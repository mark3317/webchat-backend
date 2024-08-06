package ru.markn.webchat.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import ru.markn.webchat.utils.NotEmptyOrNull

@Schema(description = "Request object for updating user information")
data class UserUpdateDto(
    @Schema(description = "User ID", example = "1")
    @field:Min(1, message = "Id must be greater than 0")
    val id: Long,

    @Schema(description = "New username for the user", example = "newuser")
    @field:NotEmptyOrNull(message = "Username cannot be empty")
    val username: String?,

    @Schema(description = "New password of the user", example = "newpassword123")
    @field:NotEmptyOrNull(message = "Password cannot be empty")
    val password: String?,

    @Schema(description = "New email of the user", example = "updateduser@example.com")
    @field:NotEmptyOrNull(message = "Email cannot be empty")
    @field:Email(message = "Invalid email format")
    val email: String?,

    @Schema(description = "Roles assigned to the user", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    @field:NotEmptyOrNull(message = "List role cannot be empty")
    val roles: List<String>?
)
