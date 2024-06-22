package ru.markn.webchat.servicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.markn.webchat.models.User;
import ru.markn.webchat.repository.UserRepository;
import java.util.List;

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
    public void saveUser(User user)
    {
        userRepository.save(user);
    }

    @Override
    public User getUser(long id)
    {
        return userRepository.getReferenceById(id);
    }
}
