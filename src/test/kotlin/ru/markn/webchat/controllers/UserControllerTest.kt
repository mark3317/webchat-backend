package ru.markn.webchat.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import ru.markn.webchat.dtos.SingUpRequest
import ru.markn.webchat.dtos.UserUpdateDto
import ru.markn.webchat.exceptions.UserNotFoundException
import ru.markn.webchat.models.User
import ru.markn.webchat.servicies.UserServiceImpl

@WebMvcTest(UserController::class)
@AutoConfigureMockMvc
@Transactional
open class UserControllerTest {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl
    private val objectMapper = jacksonObjectMapper()

    @BeforeEach
    fun setUp() {
        val userDto = SingUpRequest("testname","testpassword","test@test.com")
        userServiceImpl.addUser(userDto)
    }

    @Test
    @Transactional
    fun `test get all users`(){
        val response: MockHttpServletResponse = mockMvc.perform(
            MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
            .response

        val user: List<User> = objectMapper.readValue(response.contentAsString)

        assertNotNull(user)
        assert(user.isEmpty())
    }

    @Test
    @Transactional
    fun `test get user by id`() {
        val user = userServiceImpl.users.first()
        val response: MockHttpServletResponse = mockMvc.perform(
            MockMvcRequestBuilders.get("/users/${user.id}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
            .response

        val returnedUser: User = objectMapper.readValue(response.contentAsString)
        assertNotNull(returnedUser)
        assertEquals(user.id, returnedUser.id)
        assertEquals(user.username, returnedUser.username)
    }

    @Test
    @Transactional
    fun `test update user`() {
        val user = userServiceImpl.users.first()
        val updatedUserDto = UserUpdateDto(
            id = user.id,
            username = "updateduser",
            password = "testpassword",
            email = "test@test.com",
            roles = null
        )

        mockMvc.perform(
            MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUserDto))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        val updatedUser = userServiceImpl.getUserById(user.id)
        assertEquals("updateduser", updatedUser.username)
        assertTrue(passwordEncoder.matches("testpassword", updatedUser.password))
        assertEquals("test@test.com", updatedUser.email)
    }

    @Test
    @Transactional
    fun `test delete user`() {
        val user = userServiceImpl.users.first()

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/users/${user.id}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        assertThrows<UserNotFoundException> {
            userServiceImpl.getUserById(user.id)
        }
    }
}