package ru.markn.webchat.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import ru.markn.webchat.utils.NotEmptyOrNull
import java.io.Serializable

@Schema(description = "Role entity representing a user role")
data class RoleDto(
    @Schema(description = "Unique identifier of the role", example = "1")
    @field:Min(1, message = "Role ID must be greater than 0")
    @JsonProperty("id")
    val id: Long?,
    @Schema(description = "Name of the role", example = "ROLE_USER")
    @field:NotEmptyOrNull(message = "Role name cannot be empty")
    @JsonProperty("name")
    val name: String?
) : Serializable
