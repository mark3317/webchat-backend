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
import ru.markn.webchat.dtos.CreateChatDto
import ru.markn.webchat.dtos.InviteDto
import ru.markn.webchat.dtos.ChatMessageNewDto
import ru.markn.webchat.dtos.UserPublicDto
import ru.markn.webchat.servicies.ChatService
import ru.markn.webchat.servicies.UserService
import ru.markn.webchat.utils.toDto
import ru.markn.webchat.utils.toPublicDto
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
    @CachePut(value = [RedisConfig.CHAT_ID_KEY], key = "#messageDto.chatId")
    @MessageMapping("/message")
    fun processMessage(@Valid @Payload messageDto: ChatMessageNewDto): ChatDto {
        val chatMessage = chatService.addMessage(messageDto)
        messagingTemplate.convertAndSend("/chat/${messageDto.chatId}/message", chatMessage.toDto())
        return chatService.getChatById(messageDto.chatId).toDto()
    }

    @Operation(
        summary = "Get chat by ID",
        description = "Returns chat information based on chat ID",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @Cacheable(value = [RedisConfig.CHAT_ID_KEY], key = "#id")
    @GetMapping("/{id}")
    fun getChatById(
        @PathVariable(value = "id") id: Long,
        principal: Principal
    ): ChatDto {
        val userAuth = userService.getUserByUsername(principal.name)
        return chatService.getChatByIdForUser(id, userAuth).toDto()
    }

    @Operation(
        summary = "Get users by chat ID",
        description = "Returns users information based on chat ID",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @GetMapping("/{id}/users")
    fun getUsersByChatId(@PathVariable(value = "id") id: Long): List<UserPublicDto> =
        chatService.getUsersByChatId(id).map { it.toPublicDto() }

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
        ).also { chat ->
            createChatDto.userIds.forEach { userId ->
                messagingTemplate.convertAndSendToUser(userId.toString(), "/invite", chat.toDto())
            }
        }.toDto()
    }

    @Operation(
        summary = "Leave user from chat",
        description = "Removes the user from the chat",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @CacheEvict(value = [RedisConfig.CHAT_ID_KEY], key = "#chatId")
    @DeleteMapping("/{id}/leave")
    fun leaveUserFromChat(
        @PathVariable(value = "id") chatId: Long,
        principal: Principal
    ) {
        val user = userService.getUserByUsername(principal.name)
        chatService.leaveUserFromChat(chatId, user.id)
    }

    @Operation(
        summary = "Send chat invite",
        description = "Sends an invite to users to join a chat",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @CachePut(value = [RedisConfig.CHAT_ID_KEY], key = "#inviteDto.chatId")
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