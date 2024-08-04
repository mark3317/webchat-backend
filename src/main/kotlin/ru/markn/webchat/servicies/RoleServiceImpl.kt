package ru.markn.webchat.servicies

import org.springframework.stereotype.Service
import ru.markn.webchat.exceptions.RoleNotFoundException
import ru.markn.webchat.models.Role
import ru.markn.webchat.repositories.RoleRepository

@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository
) : RoleService {
    override val roleUser: Role
        get() = roleRepository.findRoleByName("ROLE_USER")
            .orElseThrow { RoleNotFoundException("ROLE_USER") }

    override fun getRoleByName(name: String): Role = roleRepository.findRoleByName(name)
        .orElseThrow { RoleNotFoundException(name) }
}