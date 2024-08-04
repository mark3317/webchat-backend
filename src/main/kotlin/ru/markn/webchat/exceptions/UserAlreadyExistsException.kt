package ru.markn.webchat.exceptions

class UserAlreadyExistsException(
    message: String
) : RuntimeException(
    message
)