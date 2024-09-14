package ru.markn.webchat.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.markn.webchat.configurations.RedisConfig
import ru.markn.webchat.dtos.ChatDto
import ru.markn.webchat.dtos.UserDto
import ru.markn.webchat.servicies.ChatService
import ru.markn.webchat.servicies.UserService
import ru.markn.webchat.utils.toDto

@Tag(
    name = "Admin Controller",
    description = "Controller for managing users and chats (only ADMIN has access)"
)
@RestController
@RequestMapping("/admin")
class AdminController(
    private val userService: UserService,
    private val chatService: ChatService
) {
    @Operation(
        summary = "Get all users information",
        description = "Returns all users information",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @GetMapping("/users")
    fun getAllUsers(): List<UserDto> = userService.users.map { it.toDto() }

    @Operation(
        summary = "Get user information by ID",
        description = "Returns user information based on ID",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable(value = "id") id: Long): UserDto = userService.getUserById(id).toDto()

    @Operation(
        summary = "Update user information",
        description = "Updates user information based on ID",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @CacheEvict(value = [RedisConfig.USER_ID_KEY], key = "#user.id")
    @PutMapping("/users")
    fun updateUser(@Valid @RequestBody user: UserDto): UserDto = userService.updateUser(user).toDto()

    @Operation(
        summary = "Delete user by ID",
        description = "Deletes user based on ID",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @CacheEvict(value = [RedisConfig.USER_ID_KEY], key = "#id")
    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable(value = "id") id: Long) = userService.deleteUser(id)

    @Operation(
        summary = "Get chat by ID",
        description = "Returns chat information based on chat ID",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @Cacheable(value = [RedisConfig.CHAT_ID_KEY], key = "#id")
    @GetMapping("/chats/{id}")
    fun getChatById(@PathVariable(value = "id") id: Long): ChatDto = chatService.getChatById(id).toDto()

    @Operation(
        summary = "Delete chat by id",
        description = "Deletes chat based on ID",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @CacheEvict(value = [RedisConfig.CHAT_ID_KEY], key = "#id")
    @DeleteMapping("/chats/{id}")
    fun deleteChatById(@PathVariable(value = "id") id: Long) = chatService.deleteChat(id)
}