package ru.markn.webchat.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import ru.markn.webchat.dtos.UserDto
import ru.markn.webchat.dtos.UserRegisterDto
import ru.markn.webchat.models.User
import ru.markn.webchat.servicies.RoleService
import ru.markn.webchat.servicies.UserService

@RestController
@RequestMapping("/api")
class RestController(
    private val userService: UserService,
    private val roleService: RoleService
) {
    @GetMapping("/users")
    fun getAllUsers() : ResponseEntity<List<User>> = ResponseEntity.ok(userService.users)

    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable(value = "id") id: Long) = userService.getUserById(id)

    @PostMapping("/users")
    fun addUser(@RequestBody userDto: UserRegisterDto) = userService.addUser(userDto)

    @PutMapping("/users")
    fun updateUser(@RequestBody userDto: UserDto) = userService.updateUser(userDto)

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable(value = "id") id: Long) = userService.deleteUser(id)

    @GetMapping("/roles/user")
    fun getRoleUser() = roleService.roleUser
}