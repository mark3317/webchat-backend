package ru.markn.webchat.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.io.Serializable

@Schema(description = "Data transfer object for chat messages")
data class ChatMessageDto(
    @Schema(description = "ID of the chat to which the message belongs", example = "1")
    @field:NotNull(message = "Chat ID cannot be null")
    val chatId: Long,

    @Schema(description = "ID of the user sending the message", example = "1")
    @field:NotNull(message = "Sender ID cannot be null")
    val senderId: Long,

    @Schema(description = "Content of the chat message", example = "Hello, world!")
    @field:NotEmpty(message = "Content cannot be empty")
    val content: String?
) : Serializable
