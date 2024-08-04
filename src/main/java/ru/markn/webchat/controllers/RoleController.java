package ru.markn.webchat.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.markn.webchat.models.Role;
import ru.markn.webchat.servicies.IRoleService;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController
{
    private final IRoleService roleService;

    @GetMapping
    public ResponseEntity<Role> getUserRole()
    {
        return ResponseEntity.ok(roleService.getUserRole());
    }
}
