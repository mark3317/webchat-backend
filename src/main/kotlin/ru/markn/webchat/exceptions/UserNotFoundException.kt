package ru.markn.webchat.exceptions

class UserNotFoundException(
    message: String
) : RuntimeException(
    message
)