package ru.markn.webchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.markn.webchat.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
    Role findByName(String name);
}
