package ru.markn.webchat.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class SingInRequest(
    @JsonProperty("login")
    val login: String,
    @JsonProperty("password")
    val password: String
)
