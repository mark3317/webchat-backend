package ru.markn.webchat.controllers

import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import ru.markn.webchat.dtos.UserUpdateDto
import ru.markn.webchat.models.User
import ru.markn.webchat.servicies.UserService

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    fun getAllUsers(): List<User> = userService.users

    @GetMapping("/{id}")
    fun getUserById(@PathVariable(value = "id") id: Long) = userService.getUserById(id)

    @PutMapping
    fun updateUser(@RequestBody user: UserUpdateDto) = userService.updateUser(user)

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable(value = "id") id: Long) = userService.deleteUser(id)
}