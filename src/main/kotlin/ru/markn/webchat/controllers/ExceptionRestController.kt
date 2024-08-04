package ru.markn.webchat.controllers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.markn.webchat.exceptions.RoleNotFoundException
import ru.markn.webchat.exceptions.UserAlreadyExistsException
import ru.markn.webchat.exceptions.UserNotFoundException

@RestControllerAdvice
class ExceptionRestController {

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun userNotFoundHandler(ex: UserNotFoundException) = ex.message

    @ExceptionHandler(RoleNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun roleNotFoundHandler(ex: RoleNotFoundException) = ex.message

    @ExceptionHandler(UserAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun userAlreadyExistsHandler(ex: UserAlreadyExistsException) = ex.message
}