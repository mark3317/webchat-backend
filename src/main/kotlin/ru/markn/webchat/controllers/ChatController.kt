package ru.markn.webchat.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*
import ru.markn.webchat.configurations.RedisConfig
import ru.markn.webchat.dtos.ChatDto
import ru.markn.webchat.dtos.ChatMessageDto
import ru.markn.webchat.dtos.InviteDto
import ru.markn.webchat.servicies.ChatService
import ru.markn.webchat.utils.toDto

@Tag(
    name = "Chat Controller",
    description = "Controller for managing chats"
)
@RestController
@RequestMapping("/chats")
class ChatController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val chatService: ChatService
) {
    @CacheEvict(value = [RedisConfig.CHAT_ID_KEY], key = "#chatMessageDto.chatId")
    @MessageMapping("/message")
    fun processMessage(@Valid @Payload chatMessageDto: ChatMessageDto) {
        val chatMessage = chatService.addMessage(chatMessageDto)
        messagingTemplate.convertAndSend("/chat/${chatMessageDto.chatId}/message", chatMessage.toDto())
    }

    @Operation(
        summary = "Get chat by ID",
        description = "Returns chat information based on chat ID",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @Cacheable(value = [RedisConfig.CHAT_ID_KEY], key = "#id")
    @GetMapping("/{id}")
    fun getChatById(@PathVariable(value = "id") id: Long): ChatDto =
        chatService.getChatById(id).toDto()

    @Operation(
        summary = "Send chat invite",
        description = "Sends an invite to users to join a chat",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @CacheEvict(value = [RedisConfig.CHAT_ID_KEY], key = "#inviteDto.chatId ?: 0")
    @PostMapping("/invite")
    fun sendInvite(@Valid @RequestBody inviteDto: InviteDto): ChatDto {
        val chat = inviteDto.chatId?.let {
            chatService.addUsersInChat(inviteDto)
        } ?: chatService.createChat(inviteDto.recipientsId.plus(inviteDto.senderId))
        inviteDto.recipientsId.forEach {
            messagingTemplate.convertAndSendToUser(it.toString(), "/invite", chat.toDto())
        }
        inviteDto.content?.let {
            processMessage(
                ChatMessageDto(
                    id = 0,
                    chatId = chat.id,
                    senderId = inviteDto.senderId,
                    content = it
                )
            )
        }
        return chatService.getChatById(chat.id).toDto()
    }
}