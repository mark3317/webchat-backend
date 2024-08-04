package ru.markn.webchat.exceptions

class RoleNotFoundException(
    name: String
) : RuntimeException(
    "Could not find role $name"
)