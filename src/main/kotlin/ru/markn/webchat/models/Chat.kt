package ru.markn.webchat.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import java.io.Serializable

@Schema(description = "Represents a chat entity")
@Entity
@Table(name = "chats")
data class Chat(
    @Schema(description = "Unique identifier of the chat", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Schema(description = "Name of the chat", example = "General Chat")
    @Column(name = "name", nullable = false)
    val name: String,

    @Schema(description = "List of messages in the chat")
    @OneToMany(
        mappedBy = "chat",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val messages: List<ChatMessage> = emptyList(),

    @Schema(description = "List of users in the chat")
    @ManyToMany
    @JoinTable(
        name = "users_chats",
        joinColumns = [JoinColumn(name = "chat_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    @JsonIgnore
    val users: List<User>
) : Serializable {

    @get:Schema(description = "List of user IDs in the chat", example = "[1, 2, 3]")
    @get:JsonProperty
    val usersId: List<Long>
        get() = users.map { it.id }
}