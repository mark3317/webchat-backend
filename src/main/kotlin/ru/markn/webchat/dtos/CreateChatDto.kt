package ru.markn.webchat.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import ru.markn.webchat.utils.NotEmptyOrNull

@Schema(description = "Data transfer object for creating a chat")
data class CreateChatDto(
    @Schema(description = "Name of the chat", example = "My chat")
    @field:NotEmptyOrNull(message = "Chat name cannot be empty")
    val name: String?,
    @Schema(description = "List of user IDs to create a chat", example = "[1, 2, 3]")
    @field:NotEmpty(message = "User IDs cannot be empty")
    val userIds: List<Long>,
)
