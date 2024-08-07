package ru.markn.webchat.servicies

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.ANY
import org.springframework.boot.test.context.SpringBootTest
import ru.markn.webchat.exceptions.RoleNotFoundException
import ru.markn.webchat.models.Role
import ru.markn.webchat.repositories.RoleRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureTestDatabase(replace = ANY)
class RoleServiceImplTest {

    @Autowired
    private lateinit var roleService: RoleServiceImpl

    @Autowired
    private lateinit var roleServiceImpl: RoleServiceImpl
    @Autowired
    private lateinit var roleRepository: RoleRepository

    @BeforeEach
    fun setUp() {
        val roleUser = Role(name = "ROLE_USER")
        roleRepository.save(roleUser)
    }

    @Test
    fun `test role get by name`(){
        val role = roleServiceImpl.getRoleByName("ROLE_NAME")
        assertNotNull(role)
        assertEquals("ROLE_NAME",role.name)
    }

    @Test
    fun `test get role by name throws exception for non-existent role`() {
        assertThrows<RoleNotFoundException> {
            roleService.getRoleByName("ROLE_ADMIN")
        }
    }

    @Test
    fun `test roleUser`(){
        val role = roleService.roleUser
        assertNotNull(role)
        assertEquals("ROLE_NAME",role.name)
    }
}