package ru.markn.webchat.models

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "messages")
data class ChatMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    val chat: Chat,
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    val sender: User,
    @Column(name = "content", nullable = false)
    val content: String,
    @Column(name = "timestamp")
    val timestamp: Date
)
