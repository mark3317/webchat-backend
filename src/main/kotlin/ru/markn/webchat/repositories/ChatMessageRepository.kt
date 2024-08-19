package ru.markn.webchat.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.markn.webchat.models.ChatMessage

interface ChatMessageRepository : JpaRepository<ChatMessage, Long>