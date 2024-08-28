package ru.markn.webchat.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import ru.markn.webchat.configurations.RedisConfig
import ru.markn.webchat.dtos.UserDto
import ru.markn.webchat.servicies.UserService
import ru.markn.webchat.utils.toDto

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
    fun getAllUsers(): List<UserDto> = userService.users.map { it.toDto() }

    @Operation(
        summary = "Get user information by id",
        description = "Returns user information based on id"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/{id}")
    @Cacheable(value = [RedisConfig.USER_ID_KEY], key = "#id")
    fun getUserById(@PathVariable(value = "id") id: Long): UserDto = userService.getUserById(id).toDto()

    @Operation(
        summary = "Update user information",
        description = "Updates user information based on id"
    )
    @SecurityRequirement(name = "Authorization")
    @CachePut(value = [RedisConfig.USER_ID_KEY], key = "#user.id")
    @PutMapping
    fun updateUser(@Valid @RequestBody user: UserDto): UserDto = userService.updateUser(user).toDto()

    @Operation(
        summary = "Delete user by id",
        description = "Deletes user based on id"
    )
    @SecurityRequirement(name = "Authorization")
    @CacheEvict(value = [RedisConfig.USER_ID_KEY], key = "#id")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable(value = "id") id: Long) = userService.deleteUser(id)
}