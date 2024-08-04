package ru.markn.webchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.markn.webchat.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    User findByUserName(String userName);
}
