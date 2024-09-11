package ru.markn.webchat.servicies

import ru.markn.webchat.dtos.CreateChatDto
import ru.markn.webchat.dtos.InviteDto
import ru.markn.webchat.dtos.NewChatMessageDto
import ru.markn.webchat.models.Chat
import ru.markn.webchat.models.ChatMessage

interface ChatService {
    fun getChatById(id: Long): Chat
    fun createChat(createChatDto: CreateChatDto): Chat
    fun addMessage(messageDto: NewChatMessageDto) : ChatMessage
    fun addUsersInChat(inviteDto: InviteDto) : Chat
}