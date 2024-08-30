package ru.markn.webchat.models

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import java.io.Serializable

@Schema(description = "User entity representing a user in the system")
@Entity
@Table(name = "users")
data class User(
    @Schema(description = "Unique identifier of the user", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Schema(description = "Username of the user", example = "john_doe")
    @Column(name = "username", nullable = false, unique = true)
    val username: String,

    @Schema(description = "Password of the user", example = "password123")
    @Column(name = "password", nullable = false)
    val password: String,

    @Schema(description = "Email of the user", example = "john_doe@example.com")
    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Schema(description = "Roles assigned to the user")
    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: Collection<Role>
) : Serializable
