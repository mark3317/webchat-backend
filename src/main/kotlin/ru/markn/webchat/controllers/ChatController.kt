package ru.markn.webchat.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import ru.markn.webchat.dtos.ChatMessageDto
import ru.markn.webchat.dtos.InviteDto
import ru.markn.webchat.models.Chat
import ru.markn.webchat.servicies.ChatService

@Tag(
    name = "Chat Controller",
    description = "Controller for managing chats"
)
@Controller
@RequestMapping("/chats")
class ChatController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val chatService: ChatService
) {
    @MessageMapping("/message")
    fun processMessage(@Valid @Payload chatMessageDto: ChatMessageDto) {
        val chatMessage = chatService.addMessage(chatMessageDto)
        messagingTemplate.convertAndSend("/chat/${chatMessageDto.chatId}/message", chatMessage)
    }

    @Operation(
        summary = "Get chat by ID",
        description = "Returns chat information based on chat ID",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @GetMapping("/{id}")
    fun getChatById(@PathVariable(value = "id") id: Long) = ResponseEntity.ok(chatService.getChatById(id))

    @Operation(
        summary = "Send chat invite",
        description = "Sends an invite to users to join a chat",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @PostMapping("/invite")
    fun sendInvite(@Valid @RequestBody inviteDto: InviteDto): ResponseEntity<Chat> {
        var chat = chatService.addUsersInChat(inviteDto)
        inviteDto.recipientsId.forEach {
            messagingTemplate.convertAndSendToUser(it.toString(), "/invite", chat)
        }
        inviteDto.content?.let {
            processMessage(ChatMessageDto(chat.id, inviteDto.senderId, it))
            chat = chatService.getChatById(chat.id)
        }
        return ResponseEntity.ok(chat)
    }
}