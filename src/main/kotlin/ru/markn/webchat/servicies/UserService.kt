package ru.markn.webchat.servicies

import org.springframework.security.core.userdetails.UserDetailsService
import ru.markn.webchat.dtos.UserSaveDto
import ru.markn.webchat.dtos.UserDto
import ru.markn.webchat.models.User

interface UserService : UserDetailsService {
    val users: List<User>
    fun getUserById(id: Long): User
    fun getUsersById(ids: List<Long>): List<User>
    fun getUserByUsername(username: String): User
    fun getUsersByUsernameIn(usernames: List<String>): List<User>
    fun getUserByEmail(email: String): User
    fun addUser(userDto: UserSaveDto): User
    fun updateUser(userDto: UserDto): User
    fun deleteUser(id: Long)
}