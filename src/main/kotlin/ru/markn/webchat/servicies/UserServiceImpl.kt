package ru.markn.webchat.servicies

import jakarta.transaction.Transactional
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.markn.webchat.dtos.UserUpdateDto
import ru.markn.webchat.dtos.SingUpRequest
import ru.markn.webchat.exceptions.UserAlreadyExistsException
import ru.markn.webchat.exceptions.UserNotFoundException
import ru.markn.webchat.models.User
import ru.markn.webchat.repositories.UserRepository

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val roleService: RoleService
) : UserService {
    override val users: List<User>
        get() = userRepository.findAll().sortedBy { it.id }

    override fun getUserById(id: Long): User = userRepository.findById(id)
        .orElseThrow { UserNotFoundException("User with id: $id not found") }

    override fun getUserByUsername(username: String): User = userRepository.findUserByUsername(username)
        .orElseThrow { UserNotFoundException("User with username: $username not found") }

    override fun getUserByEmail(email: String): User = userRepository.findUserByEmail(email)
        .orElseThrow { UserNotFoundException("User with email: $email not found") }

    @Transactional
    override fun addUser(userDto: SingUpRequest): User {
        if (userRepository.existsUserByUsername(userDto.username)) {
            throw UserAlreadyExistsException("User with username ${userDto.username} exist")
        }
        if (userRepository.existsUserByEmail(userDto.email)) {
            throw UserAlreadyExistsException("User with email ${userDto.email} exist")
        }
        val user = User(
            username = userDto.username,
            password = userDto.password,
            email = userDto.email,
            roles = listOf(roleService.roleUser)
        )
        return userRepository.save(user)
    }

    @Transactional
    override fun updateUser(userDto: UserUpdateDto): User {
        val oldUser = userRepository.findById(userDto.id)
            .orElseThrow { UserNotFoundException("User with id: ${userDto.id} not found") }
        val password = userDto.password ?: oldUser.password
        val username = userDto.username?.let {
            userRepository.findUserByUsername(it).ifPresent { existingUser ->
                if (existingUser.id != userDto.id) {
                    throw UserAlreadyExistsException("User with username $it exist")
                }
            }
            it
        } ?: oldUser.username
        val email = userDto.email?.let {
            userRepository.findUserByEmail(it).ifPresent { existingUser ->
                if (existingUser.id != userDto.id) {
                    throw UserAlreadyExistsException("User with email $it exist")
                }
            }
            it
        } ?: oldUser.email
        val roles = userDto.roles?.map { roleName -> roleService.getRoleByName(roleName) } ?: oldUser.roles

        val user = User(
            id = userDto.id,
            username = username,
            password = password,
            email = email,
            roles = roles
        )
        return userRepository.save(user)
    }

    @Transactional
    override fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw UserNotFoundException("User with id: $id not found")
        }
        userRepository.deleteById(id)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findUserByUsername(username)
            .orElseThrow { UserNotFoundException("User with username: $username not found") }
        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            user.roles.map { role -> SimpleGrantedAuthority(role.name) }
        )
    }
}