package ru.markn.webchat.servicies

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.markn.webchat.models.Role
import ru.markn.webchat.repositories.RoleRepository

@Transactional
@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository
) : RoleService {

    companion object {
        private const val ROLE_USER = "ROLE_USER"
        private const val ROLE_ADMIN = "ROLE_ADMIN"
    }

    override val roleUser: Role
        get() = roleRepository.getByName(ROLE_USER)

    override val roleAdmin: Role
        get() = roleRepository.getByName(ROLE_ADMIN)

    override fun getRoleByName(name: String): Role = roleRepository.getByName(name)
}