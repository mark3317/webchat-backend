package ru.markn.webchat.servicies

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import ru.markn.webchat.dtos.SingInRequest
import ru.markn.webchat.dtos.SingUpRequest
import ru.markn.webchat.dtos.UserUpdateDto
import ru.markn.webchat.exceptions.UserNotFoundException
import ru.markn.webchat.repositories.UserRepository

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserServiceImplTest {

    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var roleService: RoleService
    private lateinit var passwordEncoder: PasswordEncoder

    @BeforeEach
    fun setUp() {
        passwordEncoder = BCryptPasswordEncoder()
    }

    @Test
    @Transactional
    fun `test add user`(){
        val userDto = SingUpRequest(username = "testuser", password = "testpassword", email = "test@test.com")
        val user = userServiceImpl.addUser(userDto)

        assertNotNull(user)
        assertEquals("testuser",user.username)
        assertTrue(passwordEncoder.matches("testpassword", user.password))
        assertEquals("test@test.com",user.email)
    }

    @Test
    @Transactional
    fun `test update user`(){
        val userDto = SingUpRequest(username = "testuser",
            password = "testpassword", email = "test@test.com")

        val user = userServiceImpl.addUser(userDto)
        val updateDto = UserUpdateDto(id = user.id, username = "updateduser",
            password = "testpassword", email = "test@test.com", roles = null)

        val updatedUser = userServiceImpl.updateUser(updateDto)

        assertNotNull(user)
        assertEquals("testuser",user.username)
        assertTrue(passwordEncoder.matches("testpassword", user.password))
        assertEquals("test@test.com",user.email)
    }

    @Test
    @Transactional
    fun `test delete user`(){
        val userDto = SingUpRequest(username = "testuser",
            password = "testpassword", email = "test@test.com")

        val user = userServiceImpl.addUser(userDto)

        userServiceImpl.deleteUser(user.id)

        assertThrows<UserNotFoundException>{userServiceImpl.getUserById(user.id)}
    }

    @Test
    fun`test load user by username`(){
        val userDto = SingUpRequest(username = "testuser",
            password = "testpassword", email = "test@test.com")

        val userDetails: UserDetails = userServiceImpl.loadUserByUsername("testname")

        assertEquals("testname",userDetails.username)
        assertTrue(passwordEncoder.matches("testpassword",userDetails.password))
    }

}