package ru.markn.webchat.utils

import ru.markn.webchat.dtos.ChatDto
import ru.markn.webchat.dtos.ChatMessageDto
import ru.markn.webchat.dtos.UserPublicDto
import ru.markn.webchat.dtos.RoleDto
import ru.markn.webchat.dtos.UserDto
import ru.markn.webchat.models.Chat
import ru.markn.webchat.models.ChatMessage
import ru.markn.webchat.models.Role
import ru.markn.webchat.models.User

fun Role.toDto() = RoleDto(
    id = id,
    name = name
)

fun ChatMessage.toDto() = ChatMessageDto(
    id = id,
    chatId = chat.id,
    senderId = sender.id,
    content = content,
    timestamp = timestamp
)

fun Chat.toDto() = ChatDto(
    id = id,
    name = name,
    messages = messages.map { it.toDto() },
    userIds = users.map { it.id }
)

fun User.toDto() = UserDto(
    id = id,
    username = username,
    password = password,
    email = email,
    roles = roles.map { it.toDto() },
    chats = chats.map { it.toDto() }
)

fun User.toPublicDto() = UserPublicDto(
    id = id,
    username = username,
    email = email
)