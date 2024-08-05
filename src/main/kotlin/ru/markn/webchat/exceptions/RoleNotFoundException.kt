package ru.markn.webchat.exceptions

class RoleNotFoundException(
    name: String
) : RuntimeException(
    "Role \"$name\" not found"
)