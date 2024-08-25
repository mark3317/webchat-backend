package ru.markn.webchat.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import ru.markn.webchat.utils.NotEmptyOrNull

@Schema(description = "Data transfer object for inviting users to a chat")
data class InviteDto(
    @Schema(description = "ID of the chat to which users are being invited", example = "1")
    val chatId: Long?,

    @Schema(description = "ID of the user sending the invite", example = "1")
    @field:NotNull(message = "Sender name cannot be empty")
    val senderId: Long,

    @Schema(description = "List of recipient user IDs", example = "[2, 3, 4]")
    @field:NotEmpty(message = "Recipients id cannot be empty")
    val recipientsId: List<Long>,

    @Schema(description = "Content of the invite message", example = "You are invited to join the chat!")
    @field:NotEmptyOrNull(message = "Content cannot be empty")
    val content: String?
)
