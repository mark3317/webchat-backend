package ru.markn.webchat.controllers

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import ru.markn.webchat.dtos.ChatMessageDto
import ru.markn.webchat.dtos.InviteDto
import ru.markn.webchat.servicies.ChatService

@Controller
class ChatController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val chatService: ChatService
) {
    @MessageMapping("/message")
    fun processMessage(@Valid @Payload chatMessageDto: ChatMessageDto) {
        val chatMessage = chatService.addMessage(chatMessageDto)
        messagingTemplate.convertAndSend("/chat/${chatMessage.chat.id}/message", chatMessage)
    }

    @MessageMapping("/invite")
    fun processInvite(@Valid @Payload inviteDto: InviteDto) {
        val updatedInviteDto = chatService.addUsersInChat(inviteDto)
        updatedInviteDto.recipientNames.forEach {
            messagingTemplate.convertAndSendToUser(it, "/invite", updatedInviteDto)
        }
        inviteDto.content?.let {
            processMessage(
                ChatMessageDto(
                    chatId = updatedInviteDto.chatId!!,
                    senderName = updatedInviteDto.senderName,
                    content = it
                )
            )
        }
    }

    @GetMapping("/chat/{chatId}")
    fun getChatById(@PathVariable(value = "chatId") chatId: Long) = ResponseEntity.ok(chatService.getChatById(chatId))
}