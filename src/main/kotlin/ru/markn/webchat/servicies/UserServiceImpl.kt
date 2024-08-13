package ru.markn.webchat.servicies

import org.hibernate.Hibernate
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.markn.webchat.configurations.RedisConfig
import ru.markn.webchat.dtos.UserUpdateDto
import ru.markn.webchat.dtos.SingUpRequest
import ru.markn.webchat.exceptions.UserAlreadyExistsException
import ru.markn.webchat.exceptions.UserNotFoundException
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
        get() {
            val users = userRepository.findAll().sortedBy { it.id }
            users.forEach { Hibernate.initialize(it.roles) }
            return users
        }

    override fun getUsersByUsernameIn(usernames: List<String>): List<User> {
        val users = userRepository.findUsersByUsernameIn(usernames)
        users.forEach { Hibernate.initialize(it.roles) }
        return users
    }

    @Cacheable(value = [RedisConfig.USER_ID_KEY], key = "#id")
    override fun getUserById(id: Long): User {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User with id: $id not found") }
        Hibernate.initialize(user.roles)
        return user
    }

    @Cacheable(value = [RedisConfig.USER_USERNAME_KEY], key = "#username")
    override fun getUserByUsername(username: String): User {
        val user = userRepository.findUserByUsername(username)
            .orElseThrow { UserNotFoundException("User with username: $username not found") }
        Hibernate.initialize(user.roles)
        return user
    }

    override fun getUserByEmail(email: String): User {
        val user = userRepository.findUserByEmail(email)
            .orElseThrow { UserNotFoundException("User with email: $email not found") }
        Hibernate.initialize(user.roles)
        return user
    }

    @Cacheable(value = [RedisConfig.USER_USERNAME_KEY], key = "#userDto.username")
    override fun addUser(userDto: SingUpRequest): User {
        if (userRepository.existsUserByUsername(userDto.username)) {
            throw UserAlreadyExistsException("User with username ${userDto.username} exist")
        }
        if (userRepository.existsUserByEmail(userDto.email)) {
            throw UserAlreadyExistsException("User with email ${userDto.email} exist")
        }
        val user = User(
            username = userDto.username,
            password = passwordEncoder.encode(userDto.password),
            email = userDto.email,
            roles = listOf(roleService.roleUser)
        )
        return userRepository.save(user)
    }

    @CachePut(value = [RedisConfig.USER_ID_KEY], key = "#userDto.id")
    override fun updateUser(userDto: UserUpdateDto): User {
        val user = userRepository.findById(userDto.id)
            .orElseThrow { UserNotFoundException("User with id: ${userDto.id} not found") }
        val username = userDto.username?.let {
            userRepository.findUserByUsername(it).ifPresent { existingUser ->
                if (existingUser.id != userDto.id) {
                    throw UserAlreadyExistsException("User with username $it exist")
                }
            }
            it
        } ?: user.username
        val email = userDto.email?.let {
            userRepository.findUserByEmail(it).ifPresent { existingUser ->
                if (existingUser.id != userDto.id) {
                    throw UserAlreadyExistsException("User with email $it exist")
                }
            }
            it
        } ?: user.email
        val password = userDto.password?.let {
            passwordEncoder.encode(it)
        } ?: user.password
        val roles = if (userDto.roles != null) {
            userDto.roles.map { roleService.getRoleByName(it) }
        } else {
            Hibernate.initialize(user.roles)
            user.roles
        }
        return userRepository.save(
            user.copy(
                username = username,
                password = password,
                email = email,
                roles = roles
            )
        )
    }

    @CacheEvict(value = [RedisConfig.USER_ID_KEY], key = "#id")
    override fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw UserNotFoundException("User with id: $id not found")
        }
        userRepository.deleteById(id)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findUserByUsername(username)
            .orElseThrow { UserNotFoundException("User with username: $username not found") }
        Hibernate.initialize(user.roles)
        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            user.roles.map { role -> SimpleGrantedAuthority(role.name) }
        )
    }
}