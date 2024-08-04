package ru.markn.webchat.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.markn.webchat.models.Role
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findRoleByName(name: String): Optional<Role>
}