package ru.markn.webchat.dtos

import jakarta.validation.constraints.NotBlank

data class InviteDto(
    val chatId: Long?,
    val content: String?,
    @field:NotBlank(message = "Sender name cannot be empty")
    val senderName: String,
    @field:NotBlank(message = "Recipient names cannot be empty")
    val recipientNames: List<String>
)
