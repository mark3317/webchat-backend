package ru.markn.webchat.servicies

import org.hibernate.Hibernate
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.markn.webchat.configurations.RedisConfig
import ru.markn.webchat.dtos.ChatMessageDto
import ru.markn.webchat.dtos.InviteDto
import ru.markn.webchat.exceptions.EntityAlreadyExistsException
import ru.markn.webchat.exceptions.EntityNotFoundException
import ru.markn.webchat.models.Chat
import ru.markn.webchat.models.ChatMessage
import ru.markn.webchat.models.User
import ru.markn.webchat.repositories.ChatMessageRepository
import ru.markn.webchat.repositories.ChatRepository
import java.sql.Timestamp

@Transactional
@Service
class ChatServiceImpl(
    private val chatRepository: ChatRepository,
    private val messageRepository: ChatMessageRepository,
    private val userService: UserService
) : ChatService {
    private val self: ChatServiceImpl
        get() = this

    @Cacheable(value = [RedisConfig.CHAT_ID_KEY], key = "#id")
    override fun getChatById(id: Long): Chat {
        val chat = chatRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Chat with id $id not found") }
        Hibernate.initialize(chat.messages)
        Hibernate.initialize(chat.users)
        return chat
    }

    @CacheEvict(value = [RedisConfig.CHAT_ID_KEY], key = "#messageDto.chatId")
    override fun addMessage(messageDto: ChatMessageDto): ChatMessage = messageRepository.save(
        ChatMessage(
            chat = self.getChatById(messageDto.chatId),
            sender = userService.getUserById(messageDto.senderId),
            content = messageDto.content ?: "",
            timestamp = Timestamp(System.currentTimeMillis())
        )
    )

    override fun createChat(users: List<User>): Chat = chatRepository.save(
        Chat(
            name = users.joinToString(", ") { it.username },
            users = users.toList(),
            messages = emptyList()
        )
    )

    @CacheEvict(value = [RedisConfig.CHAT_ID_KEY], key = "#inviteDto.chatId")
    override fun addUsersInChat(inviteDto: InviteDto): Chat {
        if (inviteDto.recipientsId.contains(inviteDto.senderId)) {
            throw EntityAlreadyExistsException("Sender can't be in recipients")
        }
        val senderUser = userService.getUserById(inviteDto.senderId)
        val invitedUsers = userService.getUsersById(inviteDto.recipientsId)
        val chat = inviteDto.chatId?.let {
            val foundChat = self.getChatById(it)
            chatRepository.save(
                foundChat.copy(
                    users = foundChat.users + invitedUsers
                )
            )
        } ?: createChat(invitedUsers.plus(senderUser))
        return chat
    }
}