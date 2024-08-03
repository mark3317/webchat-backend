package ru.markn.webchat.models;


import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;
@Data
@Entity
@Table(name = "role_users")
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> user;

}
