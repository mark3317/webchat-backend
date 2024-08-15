package ru.markn.webchat.configurations

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import ru.markn.webchat.models.User

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureWebTestClient
class SecurityConfigTest{

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `test permitAll rotes`(){
        webTestClient.get()
            .uri("/")
            .exchange()
            .expectStatus().isOk
        webTestClient.get()
            .uri("/login")
            .exchange()
            .expectStatus().isOk
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `test authenticared route`(){
        webTestClient.get()
            .uri("/profile")
            .exchange()
            .expectStatus().isOk
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `test admin route`(){
        webTestClient.get()
            .uri("users")
            .exchange()
            .expectStatus().isOk
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun`unauthorized admin route`(){
        webTestClient.get()
            .uri("users")
            .exchange()
            .expectStatus()
            .isForbidden
    }

    @Test
    fun `test unauthenticated access to protected route`(){
        webTestClient.get()
            .uri("profile")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }
}