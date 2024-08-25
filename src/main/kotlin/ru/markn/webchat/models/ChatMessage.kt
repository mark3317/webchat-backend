package ru.markn.webchat.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Schema(description = "Represents a chat message entity")
@Entity
@Table(name = "messages")
data class ChatMessage(
    @Schema(description = "Unique identifier of the chat message", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Schema(description = "Chat to which the message belongs")
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @JsonIgnore
    val chat: Chat,

    @Schema(description = "User who sent the message")
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnore
    val sender: User,

    @Schema(description = "Content of the message", example = "Hello, world!")
    @Column(name = "content", nullable = false)
    val content: String,

    @Schema(description = "Timestamp when the message was sent", example = "2023-10-01T12:34:56.789Z")
    @Column(name = "timestamp")
    val timestamp: Date? = null
) : Serializable {

    @get:Schema(description = "ID of the chat to which the message belongs", example = "1")
    @get:JsonProperty
    val chatId: Long
        get() = chat.id

    @get:Schema(description = "ID of the user who sent the message", example = "1")
    @get:JsonProperty
    val senderId: Long
        get() = sender.id
}
