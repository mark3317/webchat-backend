package ru.markn.webchat.models

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import ru.markn.webchat.configurations.RedisConfig

@RedisHash(RedisConfig.BLACKJWT_KEY)
data class BlackJwt(
    @Id
    val token: String,
    @TimeToLive
    val tokenTtl: Long
)
