package ru.markn.webchat.exceptions

class UserNotFoundException(
    id: Long
) : RuntimeException(
    "Could not find user with id: $id"
)