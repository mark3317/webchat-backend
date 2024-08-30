package ru.markn.webchat.models

import jakarta.persistence.*

@Entity
@Table(name = "chats")
data class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,
    @Column(name = "name", nullable = false)
    val name: String,
    @OneToMany(
        mappedBy = "chat",
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER
    )
    val messages: List<ChatMessage> = emptyList(),
    @ManyToMany
    @JoinTable(
        name = "users_chats",
        joinColumns = [JoinColumn(name = "chat_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val users: List<User>
)