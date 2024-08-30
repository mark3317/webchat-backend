package ru.markn.webchat.configurations

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import java.time.Duration

@Configuration
@EnableCaching(proxyTargetClass = true)
@EnableRedisRepositories
class RedisConfig {

    companion object {
        const val BLACKJWT_KEY = "blackjwt"
        const val USER_ID_KEY = "user:id"
        const val CHAT_ID_KEY = "chat:id"
    }

    @Bean
    fun redisCacheManagerBuilderCustomizer(): RedisCacheManagerBuilderCustomizer? {
        return RedisCacheManagerBuilderCustomizer { builder: RedisCacheManagerBuilder ->
            builder
                .withCacheConfiguration(
                    USER_ID_KEY,
                    cacheConfiguration().entryTtl(Duration.ofMinutes(10))
                )
                .withCacheConfiguration(
                    CHAT_ID_KEY,
                    cacheConfiguration().entryTtl(Duration.ofMinutes(10))
                )
        }
    }

    @Bean
    fun cacheConfiguration(): RedisCacheConfiguration = RedisCacheConfiguration
        .defaultCacheConfig()
        .disableCachingNullValues()
        .serializeValuesWith(SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
}