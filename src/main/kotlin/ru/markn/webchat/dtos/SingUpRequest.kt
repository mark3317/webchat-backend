package ru.markn.webchat.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class SingUpRequest(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("password")
    val password: String,
    @JsonProperty("email")
    val email: String
)