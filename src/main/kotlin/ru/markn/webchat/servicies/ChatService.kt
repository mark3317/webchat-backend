package ru.markn.webchat.servicies

import ru.markn.webchat.dtos.ChatMessageDto
import ru.markn.webchat.dtos.InviteDto
import ru.markn.webchat.models.Chat
import ru.markn.webchat.models.ChatMessage
import ru.markn.webchat.models.User

interface ChatService {
    fun getChatById(id: Long): Chat
    fun createChat(users: List<User>): Chat
    fun addMessage(messageDto: ChatMessageDto) : ChatMessage
    fun addUsersInChat(inviteDto: InviteDto) : InviteDto
}