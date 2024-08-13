package ru.markn.webchat.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.markn.webchat.models.Role
import java.util.*

interface RoleRepository : JpaRepository<Role, Long> {
    fun findRoleByName(name: String): Optional<Role>
}