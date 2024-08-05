package ru.markn.webchat.servicies

import ru.markn.webchat.dtos.UserDto
import ru.markn.webchat.dtos.UserSingUpDto
import ru.markn.webchat.models.User

interface UserService {
    val users: List<User>
    fun getUserById(id: Long): User
    fun addUser(userDto: UserSingUpDto): User
    fun updateUser(userDto: UserDto): User
    fun deleteUser(id: Long)
}