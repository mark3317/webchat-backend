package ru.markn.webchat.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import ru.markn.webchat.utils.NotEmptyOrNull

@Schema(description = "Data transfer object for inviting users to a chat")
data class InviteDto(
    @Schema(description = "ID of the user sending the invite", example = "1")
    @field:NotNull(message = "Sender name cannot be empty")
    @field:Min(value = 1, message = "Sender ID must be greater than 0")
    val senderId: Long,
    @Schema(description = "ID of the chat to which users are being invited", example = "1")
    @field:Min(value = 1, message = "Chat ID must be greater than 0")
    val chatId: Long?,
    @Schema(description = "Content of the invite message", example = "You are invited to join the chat!")
    @field:NotEmptyOrNull(message = "Content cannot be empty")
    val content: String?,
    @Schema(description = "List of recipient user IDs", example = "[2, 3, 4]")
    @field:NotEmpty(message = "Recipients id cannot be empty")
    val recipientsId: List<Long>
)
