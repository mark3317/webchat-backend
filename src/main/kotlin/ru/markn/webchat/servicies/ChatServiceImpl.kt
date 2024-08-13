package ru.markn.webchat.servicies

import org.hibernate.Hibernate
import org.springframework.stereotype.Service
import ru.markn.webchat.dtos.ChatMessageDto
import ru.markn.webchat.dtos.InviteDto
import ru.markn.webchat.models.Chat
import ru.markn.webchat.models.ChatMessage
import ru.markn.webchat.models.User
import ru.markn.webchat.repositories.ChatMessageRepository
import ru.markn.webchat.repositories.ChatRepository

@Service
class ChatServiceImpl(
    private val chatRepository: ChatRepository,
    private val messageRepository: ChatMessageRepository,
    private val userService: UserService
) : ChatService {

    override fun getChatById(id: Long): Chat {
        val chat = chatRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Chat with id $id not found") }
        Hibernate.initialize(chat.messages)
        Hibernate.initialize(chat.users)
        return chat
    }

    override fun addMessage(messageDto: ChatMessageDto): ChatMessage {
        var chatMessage = ChatMessage(
            chat = getChatById(messageDto.chatId),
            sender = userService.getUserByUsername(messageDto.senderName),
            content = messageDto.content
        )
        chatMessage = messageRepository.save(chatMessage)
        return chatMessage
    }

    override fun createChat(users: List<User>): Chat {
        users.ifEmpty { throw IllegalArgumentException("Users list is empty") }
        return chatRepository.save(
            Chat(
                name = users.joinToString(", ") { it.username },
                users = users.toList(),
                messages = emptyList()
            )
        )
    }

    override fun addUsersInChat(inviteDto: InviteDto) : InviteDto {
        val senderUser = userService.getUserByUsername(inviteDto.senderName)
        val invitedUsers = userService.getUsersByUsernameIn(inviteDto.recipientNames)
        val chat = inviteDto.chatId?.let {
            val foundChat = getChatById(it)
            foundChat.copy(
                users = foundChat.users + invitedUsers
            )
        } ?: createChat(invitedUsers.plus(senderUser))
        inviteDto.content?.let {
            addMessage(
                ChatMessageDto(
                    chatId = chat.id,
                    senderName = senderUser.username,
                    content = it
                )
            )
        }
        return inviteDto.copy(chatId = inviteDto.chatId ?: chat.id)
    }
}