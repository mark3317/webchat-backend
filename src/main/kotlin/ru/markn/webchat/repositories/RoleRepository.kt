package ru.markn.webchat.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.markn.webchat.models.Role
import java.util.*

interface RoleRepository : JpaRepository<Role, Long> {
    fun getByName(name: String): Role
    fun findByName(name: String): Optional<Role>
}