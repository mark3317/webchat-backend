package ru.markn.webchat.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*
import ru.markn.webchat.configurations.RedisConfig
import ru.markn.webchat.dtos.ChatDto
import ru.markn.webchat.dtos.ChatMessageDto
import ru.markn.webchat.dtos.CreateChatDto
import ru.markn.webchat.dtos.InviteDto
import ru.markn.webchat.servicies.ChatService
import ru.markn.webchat.servicies.UserService
import ru.markn.webchat.utils.toDto
import java.security.Principal

@Tag(
    name = "Chat Controller",
    description = "Controller for managing chats"
)
@RestController
@RequestMapping("/chats")
class ChatController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val chatService: ChatService,
    private val userService: UserService
) {
    @CachePut(value = [RedisConfig.CHAT_ID_KEY], key = "#chatMessageDto.chatId")
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
        summary = "Create chat",
        description = "Creates a chat with the specified users",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @PostMapping
    fun createChat(
        @Valid @RequestBody createChatDto: CreateChatDto,
        principal: Principal
    ): ChatDto {
        val sender = userService.getUserByUsername(principal.name)
        return chatService.createChat(
            createChatDto.copy(userIds = createChatDto.userIds + sender.id)
        ).apply {
            createChatDto.userIds.forEach {
                messagingTemplate.convertAndSendToUser(it.toString(), "/invite", this.toDto())
            }
        }.toDto()
    }

    @Operation(
        summary = "Send chat invite",
        description = "Sends an invite to users to join a chat",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @CacheEvict(value = [RedisConfig.CHAT_ID_KEY], key = "#inviteDto.chatId ?: 0")
    @PostMapping("/invite")
    fun sendInvite(
        @Valid @RequestBody inviteDto: InviteDto,
        principal: Principal
    ): ChatDto {
        val sender = userService.getUserByUsername(principal.name)
        if (sender.chats.none { it.id == inviteDto.chatId }) {
            throw IllegalAccessException("You are not a member of this chat")
        }
        return chatService.addUsersInChat(inviteDto).apply {
            inviteDto.userIds.forEach {
                messagingTemplate.convertAndSendToUser(it.toString(), "/invite", this.toDto())
            }
        }.toDto()
    }
}