package ru.markn.webchat.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.markn.webchat.models.Role

interface RoleRepository : JpaRepository<Role, Long> {
    fun getByName(name: String): Role
}