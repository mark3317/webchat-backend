package ru.markn.webchat.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.io.Serializable

@Schema(description = "Data transfer object for new chat messages")
data class ChatMessageNewDto(
    @Schema(description = "ID of the chat to which the message belongs", example = "1")
    @field:NotNull(message = "Chat ID cannot be null")
    @field:Min(value = 1, message = "Chat ID must be greater than 0")
    @JsonProperty("chatId")
    val chatId: Long,
    @Schema(description = "ID of the user sending the message", example = "1")
    @field:NotNull(message = "Sender ID cannot be null")
    @field:Min(value = 1, message = "Sender ID must be greater than 0")
    @JsonProperty("senderId")
    val senderId: Long,
    @Schema(description = "Content of the chat message", example = "Hello, world!")
    @field:NotEmpty(message = "Content cannot be empty")
    @JsonProperty("content")
    val content: String,
) : Serializable
