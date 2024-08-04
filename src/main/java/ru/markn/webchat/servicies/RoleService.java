package ru.markn.webchat.servicies;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.markn.webchat.models.Role;
import ru.markn.webchat.models.User;
import ru.markn.webchat.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService
{
    private final RoleRepository roleRepository;

    @Override
    public Role getUserRole()
    {
        return roleRepository.findByName("ROLE_USER");
    }
}
