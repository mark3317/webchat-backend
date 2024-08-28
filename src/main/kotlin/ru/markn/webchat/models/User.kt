package ru.markn.webchat.models

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,
    @Column(name = "username", nullable = false, unique = true)
    val username: String,
    @Column(name = "password", nullable = false)
    val password: String,
    @Column(name = "email", nullable = false, unique = true)
    val email: String,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: List<Role>,
    @ManyToMany
    @JoinTable(
        name = "users_chats",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "chat_id")]
    )
    val chats: List<Chat> = emptyList()
)