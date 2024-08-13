package ru.markn.webchat.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.markn.webchat.models.Chat

interface ChatRepository : JpaRepository<Chat, Long>