package ru.markn.webchat.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import ru.markn.webchat.utils.NotEmptyOrNull
import java.io.Serializable

@Schema(description = "Represents a chat entity")
data class ChatDto(
    @Schema(description = "Unique identifier of the chat", example = "1")
    @field:Min(value = 1, message = "Chat ID must be greater than 0")
    @JsonProperty("id")
    val id: Long,
    @Schema(description = "Name of the chat", example = "General Chat")
    @field:NotEmptyOrNull(message = "Chat name cannot be empty")
    @JsonProperty("name")
    val name: String?,
    @Schema(description = "Messages in the chat")
    @JsonProperty("messages")
    val messages: List<ChatMessageDto>?,
    @Schema(description = "Users in the chat")
    @JsonProperty("userIds")
    val userIds: List<Long>?
) : Serializable