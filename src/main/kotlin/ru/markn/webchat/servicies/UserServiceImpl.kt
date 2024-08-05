package ru.markn.webchat.servicies

import org.springframework.stereotype.Service
import ru.markn.webchat.dtos.UserDto
import ru.markn.webchat.dtos.UserSingUpDto
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
        .orElseThrow { UserNotFoundException(id) }

    override fun addUser(userDto: UserSingUpDto): User {
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

    override fun updateUser(userDto: UserDto): User {
        val oldUser = userRepository.findById(userDto.id).orElseThrow { UserNotFoundException(userDto.id) }
        userDto.username?.let {
            userRepository.findUserByUsername(it).ifPresent { existingUser ->
                if (existingUser.id != userDto.id) {
                    throw UserAlreadyExistsException("User with username $it exist")
                }
            }
        }
        userDto.email?.let {
            userRepository.findUserByEmail(it).ifPresent { existingUser ->
                if (existingUser.id != userDto.id) {
                    throw UserAlreadyExistsException("User with email $it exist")
                }
            }
        }

        val user = User(
            id = userDto.id,
            username = userDto.username ?: oldUser.username,
            password = userDto.password ?: oldUser.password,
            email = userDto.email ?: oldUser.email,
            roles = userDto.roles?.map { roleName -> roleService.getRoleByName(roleName) } ?: oldUser.roles
        )
        return userRepository.save(user)
    }

    override fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw UserNotFoundException(id)
        }
        userRepository.deleteById(id)
    }
}