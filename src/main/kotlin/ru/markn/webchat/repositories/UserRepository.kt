package ru.markn.webchat.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.markn.webchat.models.User
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findUserByUsername(username: String): Optional<User>
    fun findUsersByUsernameIn(usernames: List<String>): List<User>
    fun findUserByEmail(email: String): Optional<User>
    fun existsUserByUsername(username: String): Boolean
    fun existsUserByEmail(email: String): Boolean
}