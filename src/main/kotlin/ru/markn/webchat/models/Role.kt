package ru.markn.webchat.models

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*

@Schema(description = "Role entity representing a user role")
@Entity
@Table(name = "roles")
data class Role(
    @Schema(description = "Unique identifier of the role", example = "1")
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Schema(description = "Name of the role", example = "ROLE_USER")
    @Column(name = "name", nullable = false, unique = true)
    val name: String
)
