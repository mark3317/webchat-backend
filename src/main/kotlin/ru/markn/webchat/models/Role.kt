package ru.markn.webchat.models

import jakarta.persistence.*

@Entity
@Table(name = "roles")
data class Role(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "name", nullable = false, unique = true)
    val name: String
)
