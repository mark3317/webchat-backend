package ru.markn.webchat.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class JwtDto(
    @JsonProperty("token")
    val token: String
)
