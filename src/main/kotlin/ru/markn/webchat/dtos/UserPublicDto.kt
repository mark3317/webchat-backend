package ru.markn.webchat.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema(description = "Public user entity representing a user in the system")
data class UserPublicDto(
    @Schema(description = "Unique identifier of the user", example = "1")
    @JsonProperty("id")
    val id: Long,

    @Schema(description = "Username of the user", example = "john_doe")
    @JsonProperty("username")
    val username: String,

    @Schema(description = "Email of the user", example = "user@gmail.com")
    @JsonProperty("email")
    val email: String,
): Serializable
