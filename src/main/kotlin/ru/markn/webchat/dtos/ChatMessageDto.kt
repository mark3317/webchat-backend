package ru.markn.webchat.dtos

import jakarta.validation.constraints.NotBlank

data class ChatMessageDto(
    @field:NotBlank(message = "Chat id cannot be empty")
    val chatId: Long,
    @field:NotBlank(message = "Sender name cannot be empty")
    val senderName: String,
    @field:NotBlank(message = "Content cannot be empty")
    val content: String
)
