package ru.markn.webchat.servicies

import org.springframework.security.core.userdetails.UserDetailsService
import ru.markn.webchat.dtos.UserUpdateDto
import ru.markn.webchat.dtos.SingUpRequest
import ru.markn.webchat.models.User

interface UserService : UserDetailsService {
    val users: List<User>
    fun getUserById(id: Long): User
    fun getUserByUsername(username: String): User
    fun getUserByEmail(email: String): User
    fun addUser(userDto: SingUpRequest): User
    fun updateUser(userDto: UserUpdateDto): User
    fun deleteUser(id: Long)
}