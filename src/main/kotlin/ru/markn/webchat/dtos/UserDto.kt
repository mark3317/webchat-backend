package ru.markn.webchat.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import ru.markn.webchat.utils.NotEmptyOrNull
import java.io.Serializable

@Schema(description = "User entity representing a user in the system")
data class UserDto(
    @Schema(description = "Unique identifier of the user", example = "1")
    @field:Min(1, message = "Id must be greater than 0")
    @JsonProperty("id")
    val id: Long,

    @Schema(description = "Username of the user", example = "john_doe")
    @field:NotEmptyOrNull(message = "Username cannot be empty")
    @JsonProperty("username")
    val username: String?,

    @Schema(description = "Password of the user", example = "password")
    @field:NotEmptyOrNull(message = "Password cannot be empty")
    @JsonProperty("password")
    val password: String?,

    @Schema(description = "Email of the user", example = "john_doe@example.com")
    @field:NotEmptyOrNull(message = "Email cannot be empty")
    @field:Email(message = "Invalid email format")
    @JsonProperty("email")
    val email: String?,

    @Schema(description = "Roles assigned to the user")
    @field:Valid
    @JsonProperty("roles")
    val roles: List<RoleDto>?,

    @Schema(description = "Chats the user is a member of")
    @field:Valid
    @JsonProperty("chats")
    val chats: List<ChatDto>?
) : Serializable
