package ru.markn.webchat.servicies

import ru.markn.webchat.dtos.CreateChatDto
import ru.markn.webchat.dtos.InviteDto
import ru.markn.webchat.dtos.ChatMessageNewDto
import ru.markn.webchat.models.Chat
import ru.markn.webchat.models.ChatMessage
import ru.markn.webchat.models.User

interface ChatService {
    fun getChatById(id: Long): Chat
    fun getChatByIdForUser(id: Long, user: User): Chat
    fun getUsersByChatId(id: Long): List<User>
    fun createChat(createChatDto: CreateChatDto): Chat
    fun addMessage(messageDto: ChatMessageNewDto) : ChatMessage
    fun addUsersInChat(inviteDto: InviteDto) : Chat
    fun leaveUserFromChat(chatId: Long, userId: Long)
    fun deleteChat(id: Long)
}