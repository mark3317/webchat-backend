package ru.markn.webchat.servicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.markn.webchat.models.User;
import ru.markn.webchat.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService
{
    private final UserRepository userRepository;

    @Override
    public void deleteUser(long id)
    {
        userRepository.deleteById(id);
    }


    @Override
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user)
    {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(long id)
    {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public User updateUser(User user)
    {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent())
        {
            return userRepository.save(user);
        } else
        {
            return null;
        }
    }

    @Override
    public User getUser(long id)
    {
        return userRepository.getReferenceById(id);
    }
}
