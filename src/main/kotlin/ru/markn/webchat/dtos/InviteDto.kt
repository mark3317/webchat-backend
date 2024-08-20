package ru.markn.webchat.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class InviteDto(
    val chatId: Long?,
    val content: String?,
    @field:NotBlank(message = "Sender name cannot be empty")
    val senderName: String,
    @field:NotEmpty(message = "Recipient names cannot be empty")
    val recipientNames: List<String>
)
