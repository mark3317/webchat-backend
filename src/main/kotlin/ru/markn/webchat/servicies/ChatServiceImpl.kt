package ru.markn.webchat.servicies

import org.hibernate.Hibernate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.markn.webchat.dtos.ChatMessageDto
import ru.markn.webchat.dtos.CreateChatDto
import ru.markn.webchat.dtos.InviteDto
import ru.markn.webchat.exceptions.EntityAlreadyExistsException
import ru.markn.webchat.exceptions.EntityNotFoundException
import ru.markn.webchat.models.Chat
import ru.markn.webchat.models.ChatMessage
import ru.markn.webchat.repositories.ChatMessageRepository
import ru.markn.webchat.repositories.ChatRepository
import java.sql.Timestamp

@Transactional
@Service
class ChatServiceImpl(
    private val chatRepository: ChatRepository,
    private val userService: UserService,
    private val messageRepository: ChatMessageRepository,
    ) : ChatService {

    override fun getChatById(id: Long): Chat = chatRepository.findById(id)
        .orElseThrow { EntityNotFoundException("Chat with id $id not found") }
        .init()

    override fun addMessage(messageDto: ChatMessageDto): ChatMessage {
        if (messageDto.content == null) {
            throw EntityNotFoundException("Message content is required")
        }
        val sender = userService.getUserById(messageDto.senderId)
        val chat = getChatById(messageDto.chatId)
        return messageRepository.save(
            ChatMessage(
                chat = chat,
                sender = sender,
                content = messageDto.content,
                timestamp = Timestamp(System.currentTimeMillis())
            )
        )
    }

    override fun createChat(createChatDto: CreateChatDto): Chat {
        val users = userService.getUsersById(createChatDto.userIds)
        return chatRepository.save(
            Chat(
                name = createChatDto.name ?: users.joinToString(", ") { it.username },
                users = users.toList(),
                messages = emptyList()
            )
        )
    }

    override fun addUsersInChat(inviteDto: InviteDto): Chat {
        val chat = chatRepository.findById(inviteDto.chatId)
            .orElseThrow { EntityNotFoundException("Chat with id ${inviteDto.chatId} not found") }
        val users = userService.getUsersById(inviteDto.userIds)
        users.forEach {
            if (chat.users.contains(it)) {
                throw EntityAlreadyExistsException("User ${it.username} is already in chat")
            }
        }
        return chatRepository.save(
            chat.copy(
                users = chat.users + users
            ).init()
        )
    }

    private fun Chat.init(): Chat {
        Hibernate.initialize(this.messages)
        Hibernate.initialize(this.users)
        return this
    }
}