package ru.markn.webchat.servicies;

import ru.markn.webchat.models.User;
import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    void saveUser(User user);
    User getUser(long id);
    void deleteUser(long id);
}
