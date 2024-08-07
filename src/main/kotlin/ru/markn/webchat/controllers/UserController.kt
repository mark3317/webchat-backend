package ru.markn.webchat.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import ru.markn.webchat.dtos.UserUpdateDto
import ru.markn.webchat.models.User
import ru.markn.webchat.servicies.UserService

@Tag(
    name = "User Controller",
    description = "Controller for managing users (only ADMIN has access)"
)
@RestController
@RequestMapping("/users")

class UserController(
    private val userService: UserService
) {
    @Operation(
        summary = "Get all users information",
        description = "Returns all users information"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping
    fun getAllUsers(): List<User> = userService.users

    @Operation(
        summary = "Get user information by id",
        description = "Returns user information based on id"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/{id}")
    fun getUserById(@PathVariable(value = "id") id: Long) = userService.getUserById(id)

    @Operation(
        summary = "Update user information",
        description = "Updates user information based on id"
    )
    @SecurityRequirement(name = "Authorization")
    @PutMapping
    fun updateUser(@Valid @RequestBody user: UserUpdateDto) = userService.updateUser(user)

    @Operation(
        summary = "Delete user by id",
        description = "Deletes user based on id"
    )
    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable(value = "id") id: Long) = userService.deleteUser(id)
}