package ru.markn.webchat.servicies

import ru.markn.webchat.models.Role

interface RoleService {
    val roleUser: Role
    val roleAdmin: Role
    fun getRoleByName(name: String): Role
}