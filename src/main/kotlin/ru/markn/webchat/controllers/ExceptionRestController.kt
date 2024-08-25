package ru.markn.webchat.controllers

import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.markn.webchat.exceptions.EntityAlreadyExistsException
import ru.markn.webchat.exceptions.EntityNotFoundException

@RestControllerAdvice
class ExceptionRestController {

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun entityNotFoundHandler(ex: EntityNotFoundException) = ex.message

    @ExceptionHandler(EntityAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun entityAlreadyExistsHandler(ex: EntityAlreadyExistsException) = ex.message

    @ExceptionHandler(BadCredentialsException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun badCredentialsHandler(ex: BadCredentialsException) = ex.message

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun methodArgumentNotValidHandler(ex: MethodArgumentNotValidException) =
        ex.bindingResult.allErrors.joinToString("\n") {"${it.defaultMessage}" }
}