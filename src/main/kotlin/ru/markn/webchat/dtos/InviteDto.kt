package ru.markn.webchat.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty

@Schema(description = "Data transfer object for inviting users to a chat")
data class InviteDto(
    @Schema(description = "ID of the chat to which users are being invited", example = "1")
    @field:Min(value = 1, message = "Chat ID must be greater than 0")
    val chatId: Long,
    @Schema(description = "List of recipient user IDs", example = "[2, 3, 4]")
    @field:NotEmpty(message = "Recipients id cannot be empty")
    val userIds: List<Long>,
)
