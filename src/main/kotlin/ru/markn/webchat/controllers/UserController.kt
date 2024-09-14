package ru.markn.webchat.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import ru.markn.webchat.configurations.RedisConfig
import ru.markn.webchat.dtos.UserPublicDto
import ru.markn.webchat.servicies.UserService
import ru.markn.webchat.utils.toPublicDto

@Tag(
    name = "User Controller",
    description = "Controller for getting users public info"
)
@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @Operation(
        summary = "Get user public information by ID",
        description = "Returns user public information based on ID"
    )
    @GetMapping("/{id}")
    @Cacheable(value = [RedisConfig.USER_ID_KEY], key = "#id")
    fun getUserById(@PathVariable(value = "id") id: Long): UserPublicDto = userService.getUserById(id).toPublicDto()

    @Operation(
        summary = "Get users public information by username match",
        description = "Returns users public information based on username"
    )
    @GetMapping
    fun findUsersByUsernameContains(@RequestParam(value = "username") username: String): List<UserPublicDto> =
        userService.findUsersByUsernameContains(username).map { it.toPublicDto() }
}