package ru.markn.webchat.servicies

import org.hibernate.Hibernate
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.markn.webchat.dtos.SingUpRequest
import ru.markn.webchat.dtos.UserDto
import ru.markn.webchat.exceptions.EntityAlreadyExistsException
import ru.markn.webchat.exceptions.EntityNotFoundException
import ru.markn.webchat.models.User
import ru.markn.webchat.repositories.UserRepository

@Transactional
@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val roleService: RoleService,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override val users: List<User>
        get() = userRepository.findAll()
            .sortedBy { it.id }
            .init()

    override fun getUserById(id: Long): User = userRepository.findById(id)
        .orElseThrow { EntityNotFoundException("User with id: $id not found") }
        .init()

    override fun getUserByUsername(username: String): User = userRepository.findByUsername(username)
        .orElseThrow { EntityNotFoundException("User with username: $username not found") }
        .init()

    override fun getUserByEmail(email: String): User = userRepository.findByEmail(email)
        .orElseThrow { EntityNotFoundException("User with email: $email not found") }
        .init()

    override fun getUsersById(ids: List<Long>): List<User> {
        val users = userRepository.findAllById(ids)
        ids.forEach { id ->
            if (!users.map { it.id }.contains(id)) {
                throw EntityNotFoundException("User with id: $id not found")
            }
        }
        return users.init()
    }

    override fun getUsersByUsernameIn(usernames: List<String>): List<User> {
        val users = userRepository.findByUsernameIn(usernames)
        usernames.forEach { username ->
            if (!users.map { it.username }.contains(username)) {
                throw EntityNotFoundException("User with username: $username not found")
            }
        }
        return users.init()
    }

    override fun addUser(userDto: SingUpRequest): User {
        if (userRepository.existsByUsername(userDto.username)) {
            throw EntityAlreadyExistsException("User with username ${userDto.username} exist")
        }
        if (userRepository.existsByEmail(userDto.email)) {
            throw EntityAlreadyExistsException("User with email ${userDto.email} exist")
        }
        val user = User(
            username = userDto.username,
            password = passwordEncoder.encode(userDto.password),
            email = userDto.email,
            roles = listOf(roleService.roleUser),
            chats = emptyList()
        )
        return userRepository.save(user)
    }

    override fun updateUser(userDto: UserDto): User {
        val user = userRepository.findById(userDto.id)
            .orElseThrow { EntityNotFoundException("User with id: ${userDto.id} not found") }
        val username = userDto.username?.let {
            userRepository.findByUsername(it).ifPresent { existingUser ->
                if (existingUser.id != userDto.id) {
                    throw EntityAlreadyExistsException("User with username $it exist")
                }
            }
            it
        } ?: user.username
        val email = userDto.email?.let {
            userRepository.findByEmail(it).ifPresent { existingUser ->
                if (existingUser.id != userDto.id) {
                    throw EntityAlreadyExistsException("User with email $it exist")
                }
            }
            it
        } ?: user.email
        val password = userDto.password?.let {
            passwordEncoder.encode(it)
        } ?: user.password
        val roles = userDto.roles?.let { roles ->
            roles.map {
                it.name?.let { roleName -> roleService.getRoleByName(roleName) }
                    ?: it.id?.let { roleId -> roleService.getRoleById(roleId) }
                    ?: throw EntityNotFoundException("Role name or id must be provided")
            }
        } ?: user.roles
        return userRepository.save(
            user.copy(
                username = username,
                password = password,
                email = email,
                roles = roles,
            ).init()
        )
    }

    override fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw EntityNotFoundException("User with id: $id not found")
        }
        userRepository.deleteById(id)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            .orElseThrow { EntityNotFoundException("User with username: $username not found") }
        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            user.roles.map { role -> SimpleGrantedAuthority(role.name) }
        )
    }

    private fun List<User>.init(): List<User> {
        this.forEach { it.init() }
        return this
    }

    private fun User.init(): User {
        this.chats.forEach { chat ->
            Hibernate.initialize(chat)
            chat.messages.forEach { Hibernate.initialize(it) }
            chat.users.forEach { Hibernate.initialize(it) }
        }
        return this
    }
}