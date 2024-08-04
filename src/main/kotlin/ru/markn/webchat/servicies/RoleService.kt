package ru.markn.webchat.servicies

import ru.markn.webchat.models.Role

interface RoleService {
    val roleUser: Role
    fun getRoleByName(name: String): Role
}