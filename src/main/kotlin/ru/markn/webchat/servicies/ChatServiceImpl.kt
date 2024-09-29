package ru.markn.webchat.servicies

import org.hibernate.Hibernate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.markn.webchat.dtos.CreateChatDto
import ru.markn.webchat.dtos.InviteDto
import ru.markn.webchat.dtos.ChatMessageNewDto
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
    private val userService: UserService,
    private val chatRepository: ChatRepository,
    private val messageRepository: ChatMessageRepository,
) : ChatService {

    override fun getChatById(id: Long): Chat = chatRepository.findById(id)
        .orElseThrow { EntityNotFoundException("Chat with id $id not found") }
        .init()

    override fun getChatByIdForUser(id: Long, user: User): Chat = getChatById(id).takeIf {
        it.users.contains(user)
    } ?: throw EntityNotFoundException("User with id ${user.id} not found in chat with id $id")

    override fun getUsersByChatId(id: Long): List<User> = getChatById(id).users

    override fun addMessage(messageDto: ChatMessageNewDto): ChatMessage {
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

    override fun leaveUserFromChat(chatId: Long, userId: Long) {
        val chat = getChatById(chatId)
        val user = userService.getUserById(userId)
        if (!chat.users.contains(user)) {
            throw EntityNotFoundException("User with id $userId not found in chat with id $chatId")
        }
        if (chat.users.size == 2) {
            deleteChat(chatId)
        } else {
            chatRepository.save(
                chat.copy(users = chat.users.filter { it.id != userId })
            )
        }
    }

    override fun deleteChat(id: Long) {
        if (!chatRepository.existsById(id)) {
            throw EntityNotFoundException("Chat with id: $id not found")
        }
        messageRepository.deleteChatMessagesByChatId(id)
        chatRepository.deleteById(id)
    }

    private fun Chat.init(): Chat {
        Hibernate.initialize(this.messages)
        Hibernate.initialize(this.users)
        return this
    }
}