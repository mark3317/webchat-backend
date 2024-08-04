package ru.markn.webchat.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDto(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("username")
    val username: String?,
    @JsonProperty("password")
    val password: String?,
    @JsonProperty("email")
    val email: String?,
    @JsonProperty("roles")
    val roles: List<String>?
)
