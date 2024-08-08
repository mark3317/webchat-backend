package ru.markn.webchat.repositories

import org.springframework.data.repository.CrudRepository
import ru.markn.webchat.models.BlackJwt

interface BlackJwtRepository : CrudRepository<BlackJwt, String>